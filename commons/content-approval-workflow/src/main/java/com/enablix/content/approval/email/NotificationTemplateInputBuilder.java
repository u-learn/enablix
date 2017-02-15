package com.enablix.content.approval.email;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.share.DocUnsecureAccessUrlPopulator;
import com.enablix.app.content.ui.format.DisplayContext;
import com.enablix.app.content.ui.format.DisplayableContentBuilder;
import com.enablix.app.content.ui.format.TextLinkProcessor;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.content.approval.model.ContentApproval;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.mail.velocity.VelocityTemplateInputResolver;
import com.enablix.core.mail.velocity.VelocityTemplateInputResolverFactory;
import com.enablix.core.ui.DisplayableContent;
import com.enablix.services.util.template.TemplateWrapper;
import com.enablix.state.change.model.ActionInput;

@Component
public class NotificationTemplateInputBuilder {

	@Autowired
	private VelocityTemplateInputResolverFactory resolverFactory;
	
	@Autowired
	private DisplayableContentBuilder displayContentBuilder;
	
	@Autowired
	private TemplateManager templateManager;
	
	@Autowired
	private DocUnsecureAccessUrlPopulator docUrlPopulator;
	
	@Autowired
	private TextLinkProcessor textLinkProcessor;
	
	public <I extends ActionInput> ContentApprovalEmailVelocityInput<I> 
			build(String actionName, I in, ContentApproval contentRequest) {
		return build(actionName, in, contentRequest, null);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <I extends ActionInput> ContentApprovalEmailVelocityInput<I> 
			build(String actionName, I in, ContentApproval contentRequest, String recipientUserId,
					String recipientEmailId) {
		
		ContentApprovalEmailVelocityInput input = new ContentApprovalEmailVelocityInput<I>(actionName, in, contentRequest);
		input.setRecipientUserId(recipientUserId);
		input.setContentRequest(contentRequest);
		
		String templateId = ProcessContext.get().getTemplateId();
		TemplateWrapper template = templateManager.getTemplateWrapper(templateId);
		ContentDataRecord dataRecord = new ContentDataRecord(templateId, 
				contentRequest.getObjectRef().getContentQId(), contentRequest.getObjectRef().getData());
		
		DisplayContext ctx = new DisplayContext();
		DisplayableContent displayableContent = displayContentBuilder.build(template, dataRecord, ctx);
		
		input.setContentData(displayableContent);
		
		Collection<VelocityTemplateInputResolver<ContentApprovalEmailVelocityInput>> resolvers = 
				resolverFactory.getResolvers(input);
		
		for (VelocityTemplateInputResolver<ContentApprovalEmailVelocityInput> resolver : resolvers) {
			resolver.work(input);
		}
		
		if (!StringUtil.isEmpty(recipientEmailId)) {
			docUrlPopulator.process(displayableContent, recipientEmailId);
			textLinkProcessor.process(displayableContent, template, recipientEmailId);
		}
		
		return input;
		
	}
	
	public <I extends ActionInput> ContentApprovalEmailVelocityInput<I> 
			build(String actionName, I in, ContentApproval contentRequest, String recipientUserId) {
		return build(actionName, in, contentRequest, recipientUserId, null);
	}
	
	public void prepareContentForEmailToUser(ContentApprovalEmailVelocityInput<?> velocityInput, String emailTo) {
		
		TemplateWrapper template = templateManager.getTemplateWrapper(ProcessContext.get().getTemplateId());
		
		docUrlPopulator.process(velocityInput.getContentData(), emailTo);
		textLinkProcessor.process(velocityInput.getContentData(), template, emailTo);
	}
	
}
