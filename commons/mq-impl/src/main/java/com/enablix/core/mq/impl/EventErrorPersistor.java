package com.enablix.core.mq.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.core.mq.Event;
import com.enablix.core.mq.EventInterceptorExecOrder;
import com.enablix.core.mq.EventListener;
import com.enablix.core.mq.model.FailedEvent;
import com.enablix.core.mq.repo.FailedEventRepository;

@Component
public class EventErrorPersistor extends EventInterceptorSupport {

	@Autowired
	private FailedEventRepository repo;
	
	@Override
	public void onProcessingError(Event<?> event, Throwable error, EventListener listener) {
		
		FailedEvent failedEvent = new FailedEvent();
		
		failedEvent.setEvent(event);
		failedEvent.setErrorMessage(error.getMessage());
		failedEvent.setHandlerId(listener.handlerId());
		
		repo.save(failedEvent);
	}
	
	@Override
	public EventInterceptorExecOrder order() {
		return EventInterceptorExecOrder.EVENT_ERROR_PERSISTOR_ORDER;
	}

}
