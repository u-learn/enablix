package com.enablix.analytics.correlation;

import java.util.Collection;

public interface ItemItemCorrelatorRegistry {

	Collection<ItemItemCorrelator> getCorrelators();
	
}
