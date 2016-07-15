package com.enablix.trigger.lifecycle.action.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.beans.SpringBackedAbstractFactory;
import com.enablix.core.commons.xsdtopojo.BaseEmailRecipientType;

@SuppressWarnings("rawtypes")
@Component
public class EmailRecipientResolverFactoryImpl extends SpringBackedAbstractFactory<EmailRecipientResolver> implements EmailRecipientResolverFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailRecipientResolverFactoryImpl.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends BaseEmailRecipientType> EmailRecipientResolver<T> getResolver(T emailContentType) {
		
		for (EmailRecipientResolver<?> resolver : registeredInstances()) {
			if (resolver.canHandle(emailContentType)) {
				return (EmailRecipientResolver<T>) resolver;
			}
		}
		
		LOGGER.error("No email recipient resolver found for [{}]", emailContentType.getClass().getName());
		throw new IllegalStateException("No email recipient resolver found for [" + emailContentType.getClass().getName() + "]");
	}

	@Override
	protected Class<EmailRecipientResolver> lookupForType() {
		return EmailRecipientResolver.class;
	}

}
