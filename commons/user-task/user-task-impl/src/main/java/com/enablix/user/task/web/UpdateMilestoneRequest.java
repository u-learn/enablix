package com.enablix.user.task.web;

public class UpdateMilestoneRequest {

	private String taskIdentity;
	
	private String stepId;
	
	private String milestone;

	public String getTaskIdentity() {
		return taskIdentity;
	}

	public void setTaskIdentity(String taskIdentity) {
		this.taskIdentity = taskIdentity;
	}

	public String getStepId() {
		return stepId;
	}

	public void setStepId(String stepId) {
		this.stepId = stepId;
	}

	public String getMilestone() {
		return milestone;
	}

	public void setMilestone(String milestone) {
		this.milestone = milestone;
	}
	
}
