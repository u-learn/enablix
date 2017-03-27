package com.enablix.trigger.lifecycle.action.email;

import java.util.Set;

import com.enablix.core.api.ContentDataRef;
import com.enablix.core.commons.xsdtopojo.BaseEmailRecipientType;
import com.enablix.services.util.template.TemplateWrapper;

public interface EmailRecipientResolver<T extends BaseEmailRecipientType> {

	Set<String> resolveRecepientUsers(ContentDataRef triggerEntity, TemplateWrapper template,
			T emailRecipientDef);
	
	boolean canHandle(BaseEmailRecipientType recipientDef);
	
}
