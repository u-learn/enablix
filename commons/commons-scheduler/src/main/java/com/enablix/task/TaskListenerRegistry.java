package com.enablix.task;

import com.enablix.core.domain.scheduler.TaskConfig;

public interface TaskListenerRegistry {

	void runBeforeExecutionListeners(TaskContext context, TaskConfig taskConfig);
	
	void runAfterExecutionListeners(TaskContext context, TaskConfig taskConfig);
	
	void runOnCompletionListeners(TaskContext context, TaskConfig taskConfig);
	
	void runOnErrorListeners(Exception e, TaskContext context, TaskConfig taskConfig);

}
