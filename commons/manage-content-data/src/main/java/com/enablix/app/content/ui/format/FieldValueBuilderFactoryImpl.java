package com.enablix.app.content.ui.format;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.beans.SpringBackedAbstractFactory;
import com.enablix.core.commons.xsdtopojo.ContentItemType;

@SuppressWarnings("rawtypes")
@Component
public class FieldValueBuilderFactoryImpl extends SpringBackedAbstractFactory<FieldValueBuilder> implements FieldValueBuilderFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(FieldValueBuilderFactoryImpl.class);
	
	@Override
	public FieldValueBuilder<?, ?> getBuilder(ContentItemType fieldDef) {

		for (FieldValueBuilder<?, ?> builder : registeredInstances()) {
			if (builder.canHandle(fieldDef)) {
				return builder;
			}
		}

		LOGGER.error("No field value builder found for field type [{}]", fieldDef.getType());
		throw new IllegalStateException("No field value builder found for field type [" + fieldDef.getType() + "]");
	}

	@Override
	protected Class<FieldValueBuilder> lookupForType() {
		return FieldValueBuilder.class;
	}

}
