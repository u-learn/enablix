package com.enablix.data.view.mongo.coll;

import org.springframework.stereotype.Component;

import com.enablix.commons.util.beans.SpringBackedAbstractFactory;

@Component
public class CollectionViewBuilderHelperRegistryImpl extends SpringBackedAbstractFactory<CollectionViewBuilderHelper> implements CollectionViewBuilderHelperRegistry {

	@Override
	public CollectionViewBuilderHelper getHelper(String collectionName) {
		
		for (CollectionViewBuilderHelper delegate : registeredInstances()) {
		
			if (delegate.canHandle(collectionName)) {
				return delegate;
			}
		}
		
		return null;
	}

	@Override
	protected Class<CollectionViewBuilderHelper> lookupForType() {
		return CollectionViewBuilderHelper.class;
	}

}
