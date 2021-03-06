package com.enablix.task;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.core.domain.scheduler.TaskConfig;
import com.enablix.scheduler.repo.TaskConfigRepository;

@Component
public class TaskContextBuilder {

	private static final String TENANTID_SEP = "|";
	
	@Autowired
	private TaskConfigRepository taskRepo;
	
	public TaskContext buildContext(TaskConfig taskConfig) {
		return new MapTaskContext(taskConfig);
	}
	
	private class MapTaskContext implements TaskContext {
		
		private TaskConfig taskConfig;
		
		MapTaskContext(TaskConfig taskConfig) {
			this.taskConfig = taskConfig;
		}

		@Override
		public Object getTaskParameter(String paramName) {
			return taskParams().get(paramName);
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
			return taskParams().keySet();
		}
		
		private Map<String, Object> taskParams() {
			return taskConfig.getParameters() == null ? new HashMap<>() : taskConfig.getParameters();
		}

		@Override
		public void updateTaskParameter(String paramName, Object value) {
			
			Map<String, Object> parameters = taskConfig.getParameters();
			
			if (parameters == null) {
				parameters = new HashMap<>();
				taskConfig.setParameters(parameters);
			}
			
			parameters.put(paramName, value);
			
			taskConfig = taskRepo.save(taskConfig);
		}

		@Override
		public TaskConfig getTaskConfig() {
			return taskConfig;
		}

		@Override
		public Object getTaskParameter(String tenantId, String paramName) {
			return getTaskParameter(tenantSpecificParamName(tenantId, paramName));
		}
		
		private String tenantSpecificParamName(String tenantId, String paramName) {
			return tenantId + TENANTID_SEP + paramName;
		}

		@Override
		public <T> T getTaskParameter(String tenantId, String paramName, Class<T> valueType) {
			return getTaskParameter(tenantSpecificParamName(tenantId, paramName), valueType);
		}

		@Override
		public void updateTaskParameter(String tenantId, String paramName, Object value) {
			updateTaskParameter(tenantSpecificParamName(tenantId, paramName), value);
		}
		
	}
}
