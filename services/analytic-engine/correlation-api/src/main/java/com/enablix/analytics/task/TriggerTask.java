package com.enablix.analytics.task;

public interface TriggerTask<T extends Trigger> {

	void run(TaskChain<T> taskChain, T trigger);
	
}
