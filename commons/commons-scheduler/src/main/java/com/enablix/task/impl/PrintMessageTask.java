package com.enablix.task.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.enablix.task.PerTenantTask;
import com.enablix.task.Task;
import com.enablix.task.TaskContext;

@Component
@PerTenantTask
public class PrintMessageTask implements Task {

	private static final Logger LOGGER = LoggerFactory.getLogger(PrintMessageTask.class);
	
	@Override
	public void run(TaskContext context) {
		
		LOGGER.debug("Print message task....");
		
		for (String paramName: context.getParameterNames()) {
			LOGGER.debug("Printing parameter: name - {}, value - {}", 
					paramName, context.getTaskParameter(paramName));
		}
		
	}

	@Override
	public String taskId() {
		return "print-message";
	}

}
