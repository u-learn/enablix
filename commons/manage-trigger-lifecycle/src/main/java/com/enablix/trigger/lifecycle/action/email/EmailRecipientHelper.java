package com.enablix.trigger.lifecycle.action.email;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.core.api.ContentDataRef;
import com.enablix.core.commons.xsdtopojo.BaseEmailRecipientType;
import com.enablix.core.commons.xsdtopojo.EmailRecipientType;
import com.enablix.services.util.template.TemplateWrapper;

@Component
public class EmailRecipientHelper {

	@Autowired
	private EmailRecipientResolverFactory resolverFactory;
	
	public Set<ContentDataRef> getEmailRecipients(ContentDataRef triggerItemRef, 
			TemplateWrapper template, EmailRecipientType recepientDef) {
		
		Set<ContentDataRef> recipientUsers = new HashSet<>();
		
		resolveRecipients(triggerItemRef, template, recipientUsers, recepientDef.getCorrelatedUsers());
		resolveRecipients(triggerItemRef, template, recipientUsers, recepientDef.getAllUsers());
		
		return recipientUsers;
	}

	private <T extends BaseEmailRecipientType> void resolveRecipients(ContentDataRef triggerItemRef, 
			TemplateWrapper template, Set<ContentDataRef> recepientUsers, T recipientDef) {
		if (recipientDef != null) {
			EmailRecipientResolver<T> resolver = resolverFactory.getResolver(recipientDef);
			recepientUsers.addAll(resolver.resolveRecepientEmails(
											triggerItemRef, template, recipientDef));
		}
	}
	
}
