package com.enablix.content.approval.email;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.share.DocUnsecureAccessUrlPopulator;
import com.enablix.app.content.ui.DisplayContext;
import com.enablix.app.content.ui.DisplayableContentBuilder;
import com.enablix.app.content.ui.format.TextLinkProcessor;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.content.approval.model.ContentApproval;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.mail.velocity.VelocityTemplateInputResolver;
import com.enablix.core.mail.velocity.VelocityTemplateInputResolverFactory;
import com.enablix.core.ui.DisplayableContent;
import com.enablix.data.view.DataView;
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
			build(String actionName, I in, ContentApproval contentRequest, DataView view) {
		return build(actionName, in, contentRequest, null, view);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <I extends ActionInput> ContentApprovalEmailVelocityInput<I> 
			build(String actionName, I in, ContentApproval contentRequest, String recipientUserId,
					String recipientEmailId, DataView view) {
		
		ContentApprovalEmailVelocityInput input = new ContentApprovalEmailVelocityInput<I>(actionName, in, contentRequest);
		input.setRecipientUserId(recipientUserId);
		input.setContentRequest(contentRequest);
		
		String templateId = ProcessContext.get().getTemplateId();
		TemplateFacade template = templateManager.getTemplateFacade(templateId);
		ContentDataRecord dataRecord = new ContentDataRecord(templateId, 
				contentRequest.getObjectRef().getContentQId(), contentRequest.getObjectRef().getData());
		
		DisplayContext ctx = new DisplayContext();
		DisplayableContent displayableContent = displayContentBuilder.build(template, dataRecord, ctx);
		
		input.setContentData(displayableContent);
		
		Collection<VelocityTemplateInputResolver<ContentApprovalEmailVelocityInput>> resolvers = 
				resolverFactory.getResolvers(input);
		
		for (VelocityTemplateInputResolver<ContentApprovalEmailVelocityInput> resolver : resolvers) {
			resolver.work(input, view);
		}
		
		if (!StringUtil.isEmpty(recipientEmailId)) {
			docUrlPopulator.populateUnsecureUrl(displayableContent, recipientEmailId);
			textLinkProcessor.process(displayableContent, template, recipientEmailId);
		}
		
		return input;
		
	}
	
	public <I extends ActionInput> ContentApprovalEmailVelocityInput<I> 
			build(String actionName, I in, ContentApproval contentRequest, String recipientUserId, DataView view) {
		return build(actionName, in, contentRequest, recipientUserId, null, view);
	}
	
	public void prepareContentForEmailToUser(ContentApprovalEmailVelocityInput<?> velocityInput, String emailTo) {
		
		TemplateFacade template = templateManager.getTemplateFacade(ProcessContext.get().getTemplateId());
		
		docUrlPopulator.populateUnsecureUrl(velocityInput.getContentData(), emailTo);
		textLinkProcessor.process(velocityInput.getContentData(), template, emailTo);
	}
	
}
