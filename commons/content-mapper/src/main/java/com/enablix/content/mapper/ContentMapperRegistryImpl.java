package com.enablix.content.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.beans.SpringBackedAbstractFactory;

@Component
public class ContentMapperRegistryImpl extends SpringBackedAbstractFactory<ContentMapper> implements ContentMapperRegistry {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContentMapperRegistryImpl.class);
	
	@Override
	public ContentMapper getMapper(ExternalContent content) {
		
		for (ContentMapper mapper : registeredInstances()) {
			if (mapper.isSupported(content.getContentSource())) {
				return mapper;
			}
		}
		
		LOGGER.error("No mapper found for source [{}]", content.getContentSource());
		
		return null;
	}

	@Override
	protected Class<ContentMapper> lookupForType() {
		return ContentMapper.class;
	}

}
