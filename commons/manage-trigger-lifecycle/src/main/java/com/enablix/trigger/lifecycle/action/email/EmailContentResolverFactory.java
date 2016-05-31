package com.enablix.trigger.lifecycle.action.email;

import com.enablix.core.commons.xsdtopojo.BaseEmailContentType;

public interface EmailContentResolverFactory {

	<T extends BaseEmailContentType> EmailContentResolver<T> getResolver(T emailContentType);
	
}
