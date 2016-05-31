package com.enablix.trigger.lifecycle;

import com.enablix.core.domain.trigger.Trigger;

public interface TriggerLifecycleManagerFactory {

	<T extends Trigger> TriggerLifecycleManager<T> getTriggerLifecycleManager(T trigger);
	
}
