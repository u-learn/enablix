package com.enablix.analytics.correlation.matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.beans.SpringBackedAbstractFactory;
import com.enablix.core.commons.xsdtopojo.FilterType;

@Component
public class FilterValueResolverFactoryImpl extends SpringBackedAbstractFactory<FilterValueResolver> implements FilterValueResolverFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(FilterValueResolverFactoryImpl.class);
	
	@Override
	public FilterValueResolver getResolver(FilterType filter) {
		
		for (FilterValueResolver resolver : registeredInstances()) {
			if (resolver.canHandle(filter)) {
				return resolver;
			}
		}
		
		LOGGER.error("No resolver found of filter: [attributeId: {}]", filter.getAttributeId());
		throw new IllegalStateException("No resolver found of filter [attributeId: " + filter.getAttributeId() + "]");
	}

	@Override
	protected Class<FilterValueResolver> lookupForType() {
		return FilterValueResolver.class;
	}

}
