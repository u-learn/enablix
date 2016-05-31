package com.enablix.trigger.lifecycle;

import com.enablix.core.domain.trigger.Trigger;

public interface TriggerLifecycleManager<T extends Trigger> {

	void startLifecycle(T trigger);
	
	boolean canHandle(Trigger t);
	
}
