package com.enablix.core.mq.impl;

import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enablix.commons.util.concurrent.Executors;
import com.enablix.core.mq.Event;
import com.enablix.core.mq.EventInterceptor;
import com.enablix.core.mq.EventListener;

public class SimpleEventBus extends AbstractEventBus {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleEventBus.class);

	private static final String EVENT_BUS_THREAD_POOL_NAME = "event-bus-pool";
	
	private ExecutorService executor = Executors.newFixedThreadPool(EVENT_BUS_THREAD_POOL_NAME, 5);
	
	@Override
	protected <T> void fireEvent(Event<T> event) {
		for (EventListener listener : listenerRegistry.listeners(event.getName())) {
			executor.execute(new EventTask<T>(event, listener));	
		}
	}
	
	protected void beforeProcessingInterceptors(Event<?> event) {
		for (EventInterceptor interceptor : interceptorRegistry.interceptors()) {
			interceptor.beforeProcessing(event);
		}
	}
	
	protected void afterProcessingInterceptors(Event<?> event) {
		for (EventInterceptor interceptor : interceptorRegistry.interceptors()) {
			interceptor.afterProcessing(event);
		}
	}
	
	protected void onProcessingErrorInterceptors(Event<?> event, Throwable t, EventListener listener) {
		for (EventInterceptor interceptor : interceptorRegistry.interceptors()) {
			interceptor.onProcessingError(event, t, listener);
		}
	}
	
	protected class EventTask<T> implements Runnable {

		private Event<T> event;
		private EventListener listener;
		
		public EventTask(Event<T> event, EventListener listener) {
			this.event = event;
			this.listener = listener;
		}
		
		@Override
		public void run() {
			
			LOGGER.debug("Delegating event [{}] for processing to [{}]", event.getName(), listener.handlerId());
			
			try {

				beforeProcessingInterceptors(event);
				listener.handle(event.getPayload());
				afterProcessingInterceptors(event);
				
				LOGGER.debug("Event [{}] processing completed", event.getName());
				
			} catch (Throwable e) {
				
				LOGGER.error("Error processing event [{}]", event.getName());
				LOGGER.error("Exception: ", e);
				onProcessingErrorInterceptors(event, e, listener);
				
			} finally {
				onProcessingCompleteInterceptors(event);
			}
		}
		
		private void onProcessingCompleteInterceptors(Event<?> event) {
			for (EventInterceptor interceptor : interceptorRegistry.interceptors()) {
				interceptor.onProcessingComplete(event);
			}
		}
		
	}

}
