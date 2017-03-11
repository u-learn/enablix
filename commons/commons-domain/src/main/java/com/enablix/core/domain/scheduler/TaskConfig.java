package com.enablix.core.domain.scheduler;

import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.domain.BaseDocumentEntity;

@Document(collection = "ebx_task_config")
public class TaskConfig extends BaseDocumentEntity {

	private String name;
	
	private String taskId;
	
	private Map<String, Object> parameters;
	
	private List<String> tenantFilter;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String beanName) {
		this.taskId = beanName;
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	public List<String> getTenantFilter() {
		return tenantFilter;
	}

	public void setTenantFilter(List<String> tenantFilter) {
		this.tenantFilter = tenantFilter;
	}
	
}
