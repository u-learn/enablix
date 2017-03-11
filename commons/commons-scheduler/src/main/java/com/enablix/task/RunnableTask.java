package com.enablix.task;

import com.enablix.core.domain.scheduler.TaskConfig;

public class RunnableTask {

	private Task taskBean;
	
	private TaskConfig taskConfig;
	
	public RunnableTask(Task taskBean, TaskConfig taskConfig) {
		super();
		this.taskBean = taskBean;
		this.taskConfig = taskConfig;
	}

	public Task getTaskBean() {
		return taskBean;
	}

	public void setTaskBean(Task taskBean) {
		this.taskBean = taskBean;
	}

	public TaskConfig getTaskConfig() {
		return taskConfig;
	}

	public void setTaskConfig(TaskConfig taskConfig) {
		this.taskConfig = taskConfig;
	}
	
}
