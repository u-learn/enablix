package com.enablix.analytics.correlation.impl;

import java.util.Collection;

import org.springframework.stereotype.Component;

import com.enablix.analytics.correlation.ItemItemCorrelator;
import com.enablix.analytics.correlation.ItemItemCorrelatorRegistry;
import com.enablix.commons.util.beans.SpringBackedAbstractFactory;

@Component
public class ItemItemCorrelatorRegistryImpl extends SpringBackedAbstractFactory<ItemItemCorrelator> implements ItemItemCorrelatorRegistry {

	@Override
	public Collection<ItemItemCorrelator> getCorrelators() {
		return registeredInstances();
	}

	@Override
	protected Class<ItemItemCorrelator> lookupForType() {
		return ItemItemCorrelator.class;
	}

}
