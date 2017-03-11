package com.enablix.task.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.beans.SpringBackedBeanRegistry;
import com.enablix.task.Task;
import com.enablix.task.TaskFactory;

@Component
public class DefaultTaskFactory extends SpringBackedBeanRegistry<Task> implements TaskFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTaskFactory.class);
	
	private Map<String, Task> taskStore = new HashMap<>();
	
	@Override
	public Task getTaskBean(String taskId) {
		return taskStore.get(taskId);
	}

	@Override
	protected Class<Task> lookupForType() {
		return Task.class;
	}

	@Override
	protected void registerBean(Task bean) {
		
		Task task = taskStore.get(bean.taskId());
		
		if (task != null) {
			LOGGER.error("More than one task with id [{}]", bean.taskId());
			throw new IllegalStateException("Multiple task with same id [" + bean.taskId() + "]");
		}
		
		taskStore.put(bean.taskId(), bean);
	}

}
