package com.enablix.analytics.task;

public interface TaskChain<T extends Trigger> {

	public void doTask(T trigger);
	
}
