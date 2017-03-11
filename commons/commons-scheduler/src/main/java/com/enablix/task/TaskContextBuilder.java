package com.enablix.task;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.core.domain.scheduler.TaskConfig;
import com.enablix.scheduler.repo.TaskConfigRepository;

@Component
public class TaskContextBuilder {

	@Autowired
	private TaskConfigRepository taskRepo;
	
	public TaskContext buildContext(TaskConfig taskConfig) {
		return new MapTaskContext(taskConfig);
	}
	
	private class MapTaskContext implements TaskContext {
		
		private TaskConfig taskConfig;
		private Map<String, Object> taskParams;
		
		MapTaskContext(TaskConfig taskConfig) {
			
			this.taskConfig = taskConfig;
			this.taskParams = taskConfig.getParameters();
			
			if (taskParams == null) {
				this.taskParams = new HashMap<>();
			}
		}

		@Override
		public Object getTaskParameter(String paramName) {
			return taskParams.get(paramName);
		}

		@Override
		public <T> T getTaskParameter(String paramName, Class<T> valueType) {
			
			Object paramValue = getTaskParameter(paramName);
			
			if (paramValue != null && valueType.isInstance(paramValue)) {
				return valueType.cast(paramValue); 
			}
			
			return null;
		}

		@Override
		public Iterable<String> getParameterNames() {
			return taskParams.keySet();
		}

		@Override
		public void updateTaskParameter(String paramName, Object value) {
			
			Map<String, Object> parameters = taskConfig.getParameters();
			
			if (parameters == null) {
				parameters = new HashMap<>();
				taskConfig.setParameters(taskParams);
				taskParams = parameters;
			}
			
			parameters.put(paramName, value);
			
			taskRepo.save(taskConfig);
		}
		
	}
}
