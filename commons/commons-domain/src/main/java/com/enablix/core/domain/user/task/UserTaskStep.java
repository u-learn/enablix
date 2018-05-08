package com.enablix.core.domain.user.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserTaskStep {

	private String stepId;
	
	private String name;
	
	private String desc;
	
	private TaskStatus status = TaskStatus.PENDING;
	
	private List<String> milestonesCovered;

	private String finalMilestone;
	
	private Map<String, Object> details = new HashMap<>();
	
	public UserTaskStep() {
		this.milestonesCovered = new ArrayList<>();
	}
	
	public String getStepId() {
		return stepId;
	}

	public void setStepId(String stepId) {
		this.stepId = stepId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public List<String> getMilestonesCovered() {
		return milestonesCovered;
	}

	public void setMilestonesCovered(List<String> milestonesCovered) {
		this.milestonesCovered = milestonesCovered;
	}

	public String getFinalMilestone() {
		return finalMilestone;
	}

	public void setFinalMilestone(String finalMilestone) {
		this.finalMilestone = finalMilestone;
	}

	public Map<String, Object> getDetails() {
		return details;
	}

	public void setDetails(Map<String, Object> details) {
		this.details = details;
	}
	
}
