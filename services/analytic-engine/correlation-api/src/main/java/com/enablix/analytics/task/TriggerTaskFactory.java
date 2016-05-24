package com.enablix.analytics.task;

public interface TriggerTaskFactory {

	<T extends Trigger> TaskChain<T> getTaskChain(T trigger);
	
}
