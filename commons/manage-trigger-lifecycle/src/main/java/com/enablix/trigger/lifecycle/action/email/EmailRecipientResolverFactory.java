package com.enablix.trigger.lifecycle.action.email;

import com.enablix.core.commons.xsdtopojo.BaseEmailRecipientType;

public interface EmailRecipientResolverFactory {

	<T extends BaseEmailRecipientType> EmailRecipientResolver<T> getResolver(T emailRecipientType);
	
}
