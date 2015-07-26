package com.enablix.analytics.context.builder;

import com.enablix.analytics.context.ContentRequest;
import com.enablix.analytics.context.UserContext;

public interface UserContextBuilder<R extends ContentRequest> {

	UserContext build(R request);
	
}
