package com.enablix.data.segment.view;

import java.util.Collection;

import org.springframework.stereotype.Component;

import com.enablix.commons.util.beans.SpringBackedAbstractFactory;

@SuppressWarnings("rawtypes")
@Component
public class DatastoreViewBuilderRegistryImpl extends SpringBackedAbstractFactory<DatastoreViewBuilder> implements DatastoreViewBuilderRegistry {

	@Override
	public Collection<DatastoreViewBuilder> getBuilders() {
		return registeredInstances();
	}

	@Override
	protected Class<DatastoreViewBuilder> lookupForType() {
		return DatastoreViewBuilder.class;
	}

}
