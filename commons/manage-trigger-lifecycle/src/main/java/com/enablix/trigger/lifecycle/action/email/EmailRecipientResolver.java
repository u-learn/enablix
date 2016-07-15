package com.enablix.trigger.lifecycle.action.email;

import java.util.Set;

import com.enablix.core.api.ContentDataRef;
import com.enablix.core.commons.xsdtopojo.BaseEmailRecipientType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;

public interface EmailRecipientResolver<T extends BaseEmailRecipientType> {

	Set<ContentDataRef> resolveRecepientEmails(ContentDataRef triggerEntity, ContentTemplate template,
			T emailRecipientDef);
	
	boolean canHandle(BaseEmailRecipientType recipientDef);
	
}
