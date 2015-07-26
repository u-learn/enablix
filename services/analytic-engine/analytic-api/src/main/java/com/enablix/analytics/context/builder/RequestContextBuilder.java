package com.enablix.analytics.context.builder;

import com.enablix.analytics.context.ContentRequest;
import com.enablix.analytics.context.RequestContext;

public interface RequestContextBuilder<R extends ContentRequest> {

	RequestContext build(R request);
	
}
