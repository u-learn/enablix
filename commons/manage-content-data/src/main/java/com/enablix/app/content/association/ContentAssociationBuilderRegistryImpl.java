package com.enablix.app.content.association;

import java.util.Collection;

import org.springframework.stereotype.Component;

import com.enablix.commons.util.beans.SpringBackedAbstractFactory;

@Component
public class ContentAssociationBuilderRegistryImpl 
		extends SpringBackedAbstractFactory<ContentAssociationBuilder> 
		implements ContentAssociationBuilderRegistry {

	@Override
	public Collection<ContentAssociationBuilder> builders() {
		return registeredInstances();
	}

	@Override
	protected Class<ContentAssociationBuilder> lookupForType() {
		return ContentAssociationBuilder.class;
	}

}
