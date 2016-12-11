package com.enablix.analytics.correlation;

import com.enablix.analytics.correlation.context.CorrelationContext;
import com.enablix.core.api.ContentDataRef;

public interface ItemUserCorrelator {

	void correlateUsers(ContentDataRef item, CorrelationContext context);
	
	void deleteCorrelationForItem(ContentDataRef item);
	
}
