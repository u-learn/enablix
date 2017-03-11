package com.enablix.task;

public interface Task {

	void run(TaskContext context);
	
	String taskId();
	
}
