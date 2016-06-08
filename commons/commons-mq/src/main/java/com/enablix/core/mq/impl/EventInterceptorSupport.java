package com.enablix.core.mq.impl;

import com.enablix.core.mq.Event;
import com.enablix.core.mq.EventInterceptor;

public abstract class EventInterceptorSupport implements EventInterceptor {

	@Override
	public void beforeProcessing(Event<?> event) {
		// empty implementation
	}

	@Override
	public void afterProcessing(Event<?> event) {
		// empty implementation
	}

	@Override
	public void onProcessingError(Event<?> event, Throwable error) {
		// empty implementation
	}

	@Override
	public void beforePublishing(Event<?> event) {
		// empty implementation
	}

	@Override
	public void afterPublishing(Event<?> event) {
		// empty implementation
	}
	
	@Override
	public void onProcessingComplete(Event<?> event) {
		// empty implementation
	}

}
