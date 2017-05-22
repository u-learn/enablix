package com.enablix.core.mq.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.enablix.core.mq.EventListener;
import com.enablix.core.mq.EventListenerRegistry;

@Component
class DefaultEventListenerRegistry implements EventListenerRegistry {

	private static final Logger LOGGER = LoggerFactory.getLogger(EventListenerRegistry.class);

	private final Map<String, Set<EventListener>> registry = new HashMap<String, Set<EventListener>>();

	private final Semaphore semaphore = new Semaphore(1);

	@Override
	public void deregister(final String eventName, final EventListener listener) {

		try {

			this.semaphore.acquire();

			final Set<EventListener> set = this.listeners(eventName);
			if (set != null) {
				set.remove(listener);
			}

			LOGGER.trace("Removed listener {} for event {}", listener.handlerId(), eventName);

		} catch (final InterruptedException e) {

			LOGGER.error("Failed to remove listener {} for event {}", listener.handlerId(), eventName);

		} finally {
			this.semaphore.release();
		}
	}

	@Override
	public Set<EventListener> listeners(final String eventName) {
		final Set<EventListener> listeners = this.registry.get(eventName);
		LOGGER.trace("Fetching listener for event {}", eventName);
		return Collections.unmodifiableSet(listeners == null ? new HashSet<EventListener>() : listeners);
	}

	@Override
	public void register(final String eventName, final EventListener listener) {
		
		try {
		
			this.semaphore.acquire();
			
			Set<EventListener> set = this.registry.get(eventName);
			if (set == null) {
				set = new HashSet<EventListener>();
				this.registry.put(eventName, set);
			}
			set.add(listener);
			
			LOGGER.trace("Added listener {} for event {}", listener.handlerId(), eventName);
			
		} catch (final InterruptedException e) {
			
			LOGGER.error("Failed to add listener {} for event {}", listener.handlerId(), eventName);
			
		} finally {
			this.semaphore.release();
		}
	}

}