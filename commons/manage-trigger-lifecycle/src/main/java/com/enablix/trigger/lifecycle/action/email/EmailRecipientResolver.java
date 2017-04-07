package com.enablix.trigger.lifecycle.action.email;

import java.util.Set;

import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.BaseEmailRecipientType;

public interface EmailRecipientResolver<T extends BaseEmailRecipientType> {

	Set<String> resolveRecepientUsers(ContentDataRef triggerEntity, TemplateFacade template,
			T emailRecipientDef);
	
	boolean canHandle(BaseEmailRecipientType recipientDef);
	
}
