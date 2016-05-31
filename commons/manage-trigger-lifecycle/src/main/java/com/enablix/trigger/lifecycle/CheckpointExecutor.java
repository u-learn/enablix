package com.enablix.trigger.lifecycle;

import com.enablix.core.domain.trigger.LifecycleCheckpoint;
import com.enablix.core.domain.trigger.Trigger;

public interface CheckpointExecutor<T extends Trigger> {

	void execute(LifecycleCheckpoint<T> checkpoint);
	
}
