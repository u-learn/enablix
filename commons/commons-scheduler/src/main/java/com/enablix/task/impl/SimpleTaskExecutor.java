package com.enablix.task.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.enablix.commons.constants.AppConstants;
import com.enablix.core.domain.scheduler.TaskConfig;
import com.enablix.task.RunnableTask;
import com.enablix.task.TaskContext;
import com.enablix.task.TaskContextBuilder;
import com.enablix.task.TaskExecutor;
import com.enablix.task.TaskListenerRegistry;

@Component
public class SimpleTaskExecutor implements TaskExecutor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleTaskExecutor.class);
	
	@Autowired
	private TaskContextBuilder taskContextBuilder;
	
	@Autowired
	private TaskListenerRegistry taskListenerRegistry;
	
	/* (non-Javadoc)
	 * @see com.enablix.scheduler.impl.TaskExecutor#run(java.util.List)
	 */
	@Override
	public void run(List<RunnableTask> tasks) {
		for (RunnableTask task : tasks) {
			run(task);
		}
	}
	
	protected void run(RunnableTask task) {
		
		TaskConfig taskConfig = task.getTaskConfig();
		
		LOGGER.debug("Starting execution of task [{}] ...", taskConfig.getName());
		
		TaskContext taskContext = taskContextBuilder.buildContext(taskConfig);
		
		try {
			
			taskListenerRegistry.runBeforeExecutionListeners(taskContext, taskConfig);
			task.getTaskBean().run(taskContext);
			taskListenerRegistry.runOnCompletionListeners(taskContext, taskConfig);
		
		} catch (Throwable e) {
			LOGGER.error("Error while running task [" + taskConfig.getName() + "]", e);
			taskListenerRegistry.runOnErrorListeners(e, taskContext, taskConfig);
		}
		
		taskListenerRegistry.runAfterExecutionListeners(taskContext, taskConfig);
	}
	
	public void loginSystemUser() {
		
		Authentication auth = new UsernamePasswordAuthenticationToken(
				AppConstants.SYSTEM_USER_ID, null, new ArrayList<>());
		
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
	
}
