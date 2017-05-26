package com.enablix.task;

import com.enablix.core.domain.scheduler.TaskConfig;

public interface TaskContext {

	Object getTaskParameter(String paramName);
	
	Object getTaskParameter(String tenantId, String paramName);
	
	<T> T getTaskParameter(String paramName, Class<T> valueType);
	
	<T> T getTaskParameter(String tenantId, String paramName, Class<T> valueType);
	
	Iterable<String> getParameterNames();
	
	void updateTaskParameter(String paramName, Object value);
	
	void updateTaskParameter(String tenantId, String paramName, Object value);
	
	TaskConfig getTaskConfig();
	
}
