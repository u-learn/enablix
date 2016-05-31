package com.enablix.analytics.correlation.impl;

import java.util.Collection;

import org.springframework.stereotype.Component;

import com.enablix.analytics.correlation.ItemUserCorrelator;
import com.enablix.analytics.correlation.ItemUserCorrelatorRegistry;
import com.enablix.commons.util.beans.SpringBackedAbstractFactory;

@Component
public class ItemUserCorrelatorRegistryImpl extends SpringBackedAbstractFactory<ItemUserCorrelator> implements ItemUserCorrelatorRegistry {

	@Override
	public Collection<ItemUserCorrelator> getCorrelators() {
		return registeredInstances();
	}

	@Override
	protected Class<ItemUserCorrelator> lookupForType() {
		return ItemUserCorrelator.class;
	}

}
