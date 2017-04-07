package com.enablix.data.segment.view;

import java.util.Collection;

public interface DatastoreViewBuilderRegistry {

	@SuppressWarnings("rawtypes")
	Collection<DatastoreViewBuilder> getBuilders();
	
}
