package com.enablix.trigger.lifecycle.action.email;

import java.util.Set;

import com.enablix.core.api.ContentDataRef;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.commons.xsdtopojo.EmailCorrelatedUsersType;

public interface EmailRecepientResolver {

	Set<ContentDataRef> resolveRecepientEmails(ContentDataRef triggerEntity, ContentTemplate template,
			EmailCorrelatedUsersType emailCorrUsersDef);
	
}
