package com.enablix.trigger.lifecycle.action.email;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.BaseEmailRecipientType;
import com.enablix.core.commons.xsdtopojo.EmailRecipientType;

@Component
public class EmailRecipientHelper {

	@Autowired
	private EmailRecipientResolverFactory resolverFactory;
	
	public Set<String> getEmailRecipients(ContentDataRef triggerItemRef, 
			TemplateFacade template, EmailRecipientType recepientDef) {
		
		Set<String> recipientUsers = new HashSet<>();
		
		resolveRecipients(triggerItemRef, template, recipientUsers, recepientDef.getCorrelatedUsers());
		resolveRecipients(triggerItemRef, template, recipientUsers, recepientDef.getAllUsers());
		
		return recipientUsers;
	}

	private <T extends BaseEmailRecipientType> void resolveRecipients(ContentDataRef triggerItemRef, 
			TemplateFacade template, Set<String> recepientUsers, T recipientDef) {
		if (recipientDef != null) {
			EmailRecipientResolver<T> resolver = resolverFactory.getResolver(recipientDef);
			recepientUsers.addAll(resolver.resolveRecepientUsers(
											triggerItemRef, template, recipientDef));
		}
	}
	
}
