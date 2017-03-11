package com.enablix.task;

import com.enablix.core.domain.scheduler.TaskConfig;

public interface TaskListener {

	void beforeTaskExecution(TaskContext context, TaskConfig taskConfig);
	
	void afterTaskExecution(TaskContext context, TaskConfig taskConfig);
	
	void onTaskCompletion(TaskContext context, TaskConfig taskConfig);
	
	void onTaskError(Exception e, TaskContext context, TaskConfig taskConfig);
	
}
