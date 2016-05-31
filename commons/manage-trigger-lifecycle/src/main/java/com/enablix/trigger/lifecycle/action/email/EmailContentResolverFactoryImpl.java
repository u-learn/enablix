package com.enablix.trigger.lifecycle.action.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.beans.SpringBackedAbstractFactory;
import com.enablix.core.commons.xsdtopojo.BaseEmailContentType;

@SuppressWarnings("rawtypes")
@Component
public class EmailContentResolverFactoryImpl extends SpringBackedAbstractFactory<EmailContentResolver> implements EmailContentResolverFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailContentResolverFactoryImpl.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends BaseEmailContentType> EmailContentResolver<T> getResolver(T emailContentType) {
		
		for (EmailContentResolver<?> resolver : registeredInstances()) {
			if (resolver.canHandle(emailContentType)) {
				return (EmailContentResolver<T>) resolver;
			}
		}
		
		LOGGER.error("No email content resolver found for [{}]", emailContentType.getClass().getName());
		throw new IllegalStateException("No email content resolver found for [" + emailContentType.getClass().getName() + "]");
	}

	@Override
	protected Class<EmailContentResolver> lookupForType() {
		return EmailContentResolver.class;
	}

}
