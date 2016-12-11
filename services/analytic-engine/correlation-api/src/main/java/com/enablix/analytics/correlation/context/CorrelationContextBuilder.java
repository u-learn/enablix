package com.enablix.analytics.correlation.context;

import com.enablix.core.api.ContentDataRef;

public interface CorrelationContextBuilder {

	CorrelationContext buildContext(ContentDataRef item);
	
}
