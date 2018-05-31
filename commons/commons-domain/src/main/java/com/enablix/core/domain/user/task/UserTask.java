package com.enablix.core.domain.user.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.domain.BaseDocumentEntity;

@Document(collection = "ebx_user_task")
public class UserTask extends BaseDocumentEntity {

	private String userId;
	
	private String code;
	
	private String name;
	
	private String desc;
	
	private List<UserTaskStep> steps;
	
	private TaskStatus status = TaskStatus.PENDING;

	private Map<String, Object> details = new HashMap<>();
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public List<UserTaskStep> getSteps() {
		return steps;
	}

	public void setSteps(List<UserTaskStep> steps) {
		this.steps = steps;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}
	
	public Map<String, Object> getDetails() {
		return details;
	}

	public void setDetails(Map<String, Object> details) {
		this.details = details;
	}

	public UserTaskStep getStep(String stepId) {
		
		if (steps != null) {
			for (UserTaskStep step : steps) {
				if (step.getStepId().equals(stepId)) {
					return step;
				}
			}
		}
		
		return null;
	}
	
}
