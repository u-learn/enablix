package com.enablix.trigger.lifecycle.action;

import com.enablix.core.domain.trigger.LifecycleCheckpoint;
import com.enablix.core.domain.trigger.Trigger;

public interface ActionChain<T extends Trigger> {

	public void doAction(LifecycleCheckpoint<T> checkpoint);
	
}
