package com.enablix.core.mq;

public interface EventBus {

	public void register(String eventName, EventListener listener);
	
	public void deregister(String eventName, EventListener listener);
	
	public <T> void publishEvent(Event<T> event);
	
}
