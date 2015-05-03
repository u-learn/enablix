package com.enablix.app.content.enrich;

import java.util.Collection;

import org.springframework.stereotype.Component;

import com.enablix.commons.util.beans.SpringBackedAbstractFactory;

@Component
public class ContentEnricherRegistryImpl extends SpringBackedAbstractFactory<ContentEnricher> 
		implements ContentEnricherRegistry {

	@Override
	public Collection<ContentEnricher> getEnrichers() {
		return registeredInstances();
	}

	@Override
	protected Class<ContentEnricher> lookupForType() {
		return ContentEnricher.class;
	}

}
