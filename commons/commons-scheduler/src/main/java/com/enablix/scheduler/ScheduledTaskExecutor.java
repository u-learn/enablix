package com.enablix.scheduler;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.core.domain.scheduler.SchedulerConfig;
import com.enablix.core.domain.scheduler.TaskConfig;
import com.enablix.scheduler.repo.SchedulerConfigRepository;
import com.enablix.scheduler.repo.TaskConfigRepository;
import com.enablix.task.PerTenantTask;
import com.enablix.task.RunnableTask;
import com.enablix.task.Task;
import com.enablix.task.TaskFactory;
import com.enablix.task.impl.PerTenantTaskExecutor;
import com.enablix.task.impl.SimpleTaskExecutor;

@Component
public class ScheduledTaskExecutor {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTaskExecutor.class);
	
	private static final String taskInterfaceName = Task.class.getCanonicalName();
	
	@Autowired
	private SchedulerConfigRepository schedulerConfigRepo;
	
	@Autowired
	private TaskFactory taskFactory;
	
	@Autowired
	private TaskConfigRepository taskRepo;
	
	@Autowired
	private SimpleTaskExecutor simpleTaskExecutor;
	
	@Autowired
	private PerTenantTaskExecutor perTenantTaskExecutor;
	
	public void executeScheduledTasks(SchedulerConfig config) {
		
		// get the latest config from database
		SchedulerConfig schedulerConfig = schedulerConfigRepo.findOne(config.getId());
		
		List<RunnableTask> tasks = getRunnableTasks(schedulerConfig);
		
		List<RunnableTask> perTenantTasks = new ArrayList<>();
		List<RunnableTask> simpleTasks = new ArrayList<>();
		
		for (RunnableTask task : tasks) {
			
			if (isPerTenantTask(task.getTaskBean())) {
				perTenantTasks.add(task);
			} else {
				simpleTasks.add(task);
			}
		}
		
		simpleTaskExecutor.run(simpleTasks);
		perTenantTaskExecutor.run(perTenantTasks);
		
	}
	
	private List<RunnableTask> getRunnableTasks(SchedulerConfig schedulerConfig) {
		
		List<RunnableTask> tasks = new ArrayList<>();
				
		List<TaskConfig> taskConfigs = taskRepo.findByIdentityIn(schedulerConfig.getTasksToExecute());
		
		for (TaskConfig taskConfig : taskConfigs) {
			
			Object bean = getBean(taskConfig);
			
			if (bean != null) {
				
				if (bean instanceof Task) {
	
					tasks.add(new RunnableTask((Task) bean, taskConfig));
					
				} else {
					LOGGER.error("Task bean [{}] should be an instance of [{}]", 
							taskConfig.getTaskId(), taskInterfaceName);
				}
				
			}
		}
		
		return tasks;
	}
	
	private boolean isPerTenantTask(Object taskBean) {
		PerTenantTask perTenantTaskAnnot = taskBean.getClass().getAnnotation(PerTenantTask.class);
		return perTenantTaskAnnot != null;
	}

	private Object getBean(TaskConfig taskConfig) {
		
		String taskId = taskConfig.getTaskId();
		Task bean = taskFactory.getTaskBean(taskId);
		
		if (bean == null) {
			LOGGER.error("No task bean found with id [{}] for task [{}]", taskId, taskConfig.getName());
		}
		
		return bean;
	}
	
}
