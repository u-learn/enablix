package com.enablix.content.mapper.xml.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.beans.SpringBackedAbstractFactory;
import com.enablix.core.commons.xsdtopojo.ContentItemType;

@SuppressWarnings("rawtypes")
@Component
public class ContentItemValueBuilderFactoryImpl extends SpringBackedAbstractFactory<ContentItemValueBuilder> implements ContentItemValueBuilderFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContentItemValueBuilderFactoryImpl.class);
	
	@Override
	public ContentItemValueBuilder<?> getBuilder(ContentItemType contentItem) {
		
		for (ContentItemValueBuilder<?> builder : registeredInstances()) {
			if (builder.canHandle(contentItem)) {
				return builder;
			}
		}
		
		LOGGER.error("No builder found for content item : [{}], type : [{}]", 
				contentItem.getQualifiedId(), contentItem.getType());
		
		throw new IllegalStateException("No builder found for content item : " 
					+ contentItem.getQualifiedId() + ", type : " + contentItem.getType());
	}

	@Override
	protected Class<ContentItemValueBuilder> lookupForType() {
		return ContentItemValueBuilder.class;
	}

}
