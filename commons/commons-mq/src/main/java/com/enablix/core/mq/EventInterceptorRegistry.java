package com.enablix.core.mq;

import java.util.Collection;

public interface EventInterceptorRegistry {

	Collection<EventInterceptor> interceptors();
	
}
