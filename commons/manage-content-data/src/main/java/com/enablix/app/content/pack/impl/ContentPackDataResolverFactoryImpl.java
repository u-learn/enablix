package com.enablix.app.content.pack.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enablix.app.content.pack.ContentPackDataResolver;
import com.enablix.app.content.pack.ContentPackDataResolverFactory;
import com.enablix.commons.util.beans.SpringBackedBeanRegistry;
import com.enablix.core.domain.content.pack.ContentPack;
import com.enablix.core.domain.content.pack.ContentPack.Type;

@SuppressWarnings("rawtypes")
@Component
public class ContentPackDataResolverFactoryImpl extends SpringBackedBeanRegistry<ContentPackDataResolver> implements ContentPackDataResolverFactory {

	private Map<ContentPack.Type, ContentPackDataResolver> registry = new HashMap<>();
	
	@Override
	public ContentPackDataResolver getResolver(Type packType) {
		return registry.get(packType);
	}

	@Override
	protected Class<ContentPackDataResolver> lookupForType() {
		return ContentPackDataResolver.class;
	}

	@Override
	protected void registerBean(ContentPackDataResolver bean) {
		registry.put(bean.resolverPackType(), bean);
	}

}
