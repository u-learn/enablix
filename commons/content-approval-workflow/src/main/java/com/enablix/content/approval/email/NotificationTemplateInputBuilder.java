package com.enablix.content.approval.email;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.ui.format.DisplayContext;
import com.enablix.app.content.ui.format.DisplayableContentBuilder;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.content.approval.model.ContentApproval;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.mail.velocity.VelocityTemplateInputResolver;
import com.enablix.core.mail.velocity.VelocityTemplateInputResolverFactory;
import com.enablix.core.ui.DisplayableContent;
import com.enablix.state.change.model.ActionInput;

@Component
public class NotificationTemplateInputBuilder {

	@Autowired
	private VelocityTemplateInputResolverFactory resolverFactory;
	
	@Autowired
	private DisplayableContentBuilder displayContentBuilder;
	
	@Autowired
	private TemplateManager templateManager;
	
	public <I extends ActionInput> ContentApprovalEmailVelocityInput<I> 
			build(String actionName, I in, ContentApproval contentRequest) {
		return build(actionName, in, contentRequest, null);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <I extends ActionInput> ContentApprovalEmailVelocityInput<I> 
			build(String actionName, I in, ContentApproval contentRequest, String recipientUserId) {
		
		ContentApprovalEmailVelocityInput input = new ContentApprovalEmailVelocityInput<I>(actionName, in, contentRequest);
		input.setRecipientUserId(recipientUserId);
		input.setContentRequest(contentRequest);
		
		ContentTemplate template = templateManager.getTemplate(ProcessContext.get().getTemplateId());
		ContentDataRecord dataRecord = new ContentDataRecord(template.getId(), 
				contentRequest.getObjectRef().getContentQId(), contentRequest.getObjectRef().getData());
		
		DisplayContext ctx = new DisplayContext();
		DisplayableContent displayableContent = displayContentBuilder.build(template, dataRecord, ctx);
		input.setContentData(displayableContent);
		
		Collection<VelocityTemplateInputResolver<ContentApprovalEmailVelocityInput>> resolvers = 
				resolverFactory.getResolvers(input);
		
		for (VelocityTemplateInputResolver<ContentApprovalEmailVelocityInput> resolver : resolvers) {
			resolver.work(input);
		}
		
		return input;
		
	}
	
}
