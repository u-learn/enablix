package com.enablix.trigger.lifecycle.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.beans.SpringBackedAbstractFactory;
import com.enablix.core.domain.trigger.Trigger;
import com.enablix.trigger.lifecycle.TriggerLifecycleManager;
import com.enablix.trigger.lifecycle.TriggerLifecycleManagerFactory;

@SuppressWarnings("rawtypes")
@Component
public class TriggerLifecycleManagerFactoryImpl extends SpringBackedAbstractFactory<TriggerLifecycleManager> implements TriggerLifecycleManagerFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(TriggerLifecycleManagerFactoryImpl.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Trigger> TriggerLifecycleManager<T> getTriggerLifecycleManager(T trigger) {
		
		for (TriggerLifecycleManager<?> lifecycleMgr : registeredInstances()) {
			if (lifecycleMgr.canHandle(trigger)) {
				return (TriggerLifecycleManager<T>) lifecycleMgr;
			}
		}
		
		LOGGER.error("No Trigger Lifecycle Manager found for trigger [{}]",  trigger.getClass().getName());
		throw new IllegalStateException("No Trigger Lifecycle Manager found for trigger [" + trigger.getClass().getName() + "]");
		
	}

	@Override
	protected Class<TriggerLifecycleManager> lookupForType() {
		return TriggerLifecycleManager.class;
	}

}
