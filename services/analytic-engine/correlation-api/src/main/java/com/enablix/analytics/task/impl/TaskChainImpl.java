package com.enablix.analytics.task.impl;

import org.springframework.util.Assert;

import com.enablix.analytics.task.TaskChain;
import com.enablix.analytics.task.Trigger;
import com.enablix.analytics.task.TriggerTask;

public class TaskChainImpl<T extends Trigger> implements TaskChain<T> {

	private TriggerTask<T>[] tasks;
	
	private int currentTaskIndx;
	
	public TaskChainImpl(TriggerTask<T>[] tasks) {
		Assert.notNull(tasks, "Tasks cannot be null");
		this.tasks = tasks;
		this.currentTaskIndx = 0;
	}
	
	@Override
	public void doTask(T trigger) {
		if (currentTaskIndx < tasks.length) {
			TriggerTask<T> currentTask = tasks[currentTaskIndx++];
			currentTask.run(this, trigger);
		}
	}

}
