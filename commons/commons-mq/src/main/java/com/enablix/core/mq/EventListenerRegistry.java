package com.enablix.core.mq;

import java.util.Set;

public interface EventListenerRegistry {

	public void register(String eventName, EventListener listener);
	
	public void deregister(String eventName, EventListener listener);

	public Set<EventListener> listeners(String eventName);
	
}
