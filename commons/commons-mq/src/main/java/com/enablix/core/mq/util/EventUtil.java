package com.enablix.core.mq.util;

import com.enablix.core.mq.Event;
import com.enablix.core.mq.EventBus;
import com.enablix.core.mq.EventListener;

public final class EventUtil {

	private static EventBus EVENT_BUS;
	
	public static void registerBus(EventBus eventBus) {
		EVENT_BUS = eventBus;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void publishEvent(Event... events) {
		if (events != null) {
			for (Event event : events) {
				EVENT_BUS.publishEvent(event);
			}
		}
	}
	
	public static EventBus eventBus() {
		return EVENT_BUS;
	}

	public static void register(String eventName, EventListener listener) {
		EVENT_BUS.register(eventName, listener);
	}

}
