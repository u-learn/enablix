package com.enablix.core.mq;

public interface EventInterceptor {

	EventInterceptorExecOrder order();
	
	void beforeProcessing(Event<?> event);
	void afterProcessing(Event<?> event);
	void onProcessingError(Event<?> event, Throwable error);
	void onProcessingComplete(Event<?> event);
	
	void beforePublishing(Event<?> event);
	void afterPublishing(Event<?> event);
	
}
