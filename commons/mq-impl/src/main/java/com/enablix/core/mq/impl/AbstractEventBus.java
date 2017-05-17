package com.enablix.core.mq.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.enablix.core.mq.Event;
import com.enablix.core.mq.EventBus;
import com.enablix.core.mq.EventInterceptor;
import com.enablix.core.mq.EventInterceptorRegistry;
import com.enablix.core.mq.EventListener;
import com.enablix.core.mq.EventListenerRegistry;
import com.enablix.core.mq.util.EventUtil;

public abstract class AbstractEventBus implements EventBus {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEventBus.class);
	
	@Autowired
	protected EventListenerRegistry listenerRegistry;
	
	@Autowired
	protected EventInterceptorRegistry interceptorRegistry;
	
	protected AbstractEventBus() {
		EventUtil.registerBus(this);
	}
	
	@Override
	public void register(String eventName, EventListener listener) {
		listenerRegistry.register(eventName, listener);
	}

	@Override
	public void deregister(String eventName, EventListener listener) {
		listenerRegistry.deregister(eventName, listener);
	}

	protected void beforePublishInterceptors(Event<?> event) {
		for (EventInterceptor interceptor : interceptorRegistry.interceptors()) {
			interceptor.beforePublishing(event);
		}
	}
	
	protected void afterPublishInterceptors(Event<?> event) {
		for (EventInterceptor interceptor : interceptorRegistry.interceptors()) {
			interceptor.afterPublishing(event);
		}
	}
	
	protected void publishErrorInterceptors(Event<?> event) {
		for (EventInterceptor interceptor : interceptorRegistry.interceptors()) {
			interceptor.afterPublishing(event);
		}
	}
	
	@Override
	public <T> void publishEvent(Event<T> event) {
		
		if (event != null) {
		
			LOGGER.debug("Publishing event [{}]", event.getName());
			
			beforePublishInterceptors(event);
			fireEvent(event);
			afterPublishInterceptors(event);
			
			LOGGER.debug("Published event [{}]", event.getName());
		}
	}
	
	protected abstract <T> void fireEvent(Event<T> event);

}
