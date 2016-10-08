package com.enablix.app.content.bounded.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.enablix.app.content.bounded.BoundedListBuilder;
import com.enablix.app.content.bounded.BoundedListBuilderFactory;
import com.enablix.commons.util.beans.SpringBackedAbstractFactory;
import com.enablix.core.commons.xsdtopojo.BoundedType;

@Component
public class DefaultBoundedListBuilderFactory extends SpringBackedAbstractFactory<BoundedListBuilder> implements BoundedListBuilderFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultBoundedListBuilderFactory.class);
	
	@Override
	public BoundedListBuilder getBuilder(BoundedType boundedType) {
		
		for (BoundedListBuilder builder : registeredInstances()) {
			if (builder.canHandle(boundedType)) {
				return builder;
			}
		}

		LOGGER.error("No handler found for bounded type {}", boundedType);
		throw new IllegalStateException("No handler found for bounded type " + boundedType);
	}

	@Override
	protected Class<BoundedListBuilder> lookupForType() {
		return BoundedListBuilder.class;
	}

}
