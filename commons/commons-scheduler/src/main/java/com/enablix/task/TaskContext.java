package com.enablix.task;

public interface TaskContext {

	Object getTaskParameter(String paramName);
	
	<T> T getTaskParameter(String paramName, Class<T> valueType);
	
	Iterable<String> getParameterNames();
	
	void updateTaskParameter(String paramName, Object value);
	
}
