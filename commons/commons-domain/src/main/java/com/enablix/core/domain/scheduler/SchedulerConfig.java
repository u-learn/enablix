package com.enablix.core.domain.scheduler;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.domain.BaseDocumentEntity;

@Document(collection = "ebx_scheduler_config")
public class SchedulerConfig extends BaseDocumentEntity {

	private String name;
	
	private String cronExpression;
	
	// identities of the task to be executed
	private List<String> tasksToExecute;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public List<String> getTasksToExecute() {
		return tasksToExecute;
	}

	public void setTasksToExecute(List<String> tasksToExecute) {
		this.tasksToExecute = tasksToExecute;
	}
	
}
