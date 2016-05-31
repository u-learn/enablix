package com.enablix.analytics.correlation;

import java.util.Collection;

public interface ItemUserCorrelatorRegistry {

	Collection<ItemUserCorrelator> getCorrelators();
	
}
