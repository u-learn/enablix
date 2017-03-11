package com.enablix.task.impl;

import org.springframework.stereotype.Component;

import com.enablix.commons.util.beans.SpringBackedAbstractFactory;
import com.enablix.core.domain.scheduler.TaskConfig;
import com.enablix.task.TaskContext;
import com.enablix.task.TaskListener;
import com.enablix.task.TaskListenerRegistry;

@Component
public class DefaultTaskListenerRegistry extends SpringBackedAbstractFactory<TaskListener> implements TaskListenerRegistry {

	@Override
	protected Class<TaskListener> lookupForType() {
		return TaskListener.class;
	}

	@Override
	public void runBeforeExecutionListeners(TaskContext context, TaskConfig taskConfig) {
		for (TaskListener listener : registeredInstances()) {
			listener.beforeTaskExecution(context, taskConfig);
		}
	}

	@Override
	public void runAfterExecutionListeners(TaskContext context, TaskConfig taskConfig) {
		for (TaskListener listener : registeredInstances()) {
			listener.afterTaskExecution(context, taskConfig);
		}
	}

	@Override
	public void runOnCompletionListeners(TaskContext context, TaskConfig taskConfig) {
		for (TaskListener listener : registeredInstances()) {
			listener.onTaskCompletion(context, taskConfig);
		}
	}

	@Override
	public void runOnErrorListeners(Exception e, TaskContext context, TaskConfig taskConfig) {
		for (TaskListener listener : registeredInstances()) {
			listener.onTaskError(e, context, taskConfig);
		}
	}

}
