package com.enablix.task.impl;

import com.enablix.core.domain.scheduler.TaskConfig;
import com.enablix.task.TaskContext;
import com.enablix.task.TaskListener;

public abstract class TaskListenerAdapter implements TaskListener {

	@Override
	public void beforeTaskExecution(TaskContext context, TaskConfig taskConfig) {
		// empty method
	}

	@Override
	public void afterTaskExecution(TaskContext context, TaskConfig taskConfig) {
		// empty method
	}

	@Override
	public void onTaskCompletion(TaskContext context, TaskConfig taskConfig) {
		// empty method
	}

	@Override
	public void onTaskError(Exception e, TaskContext context, TaskConfig taskConfig) {
		// empty method
	}

}
