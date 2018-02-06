package com.enablix.scheduler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import com.enablix.core.domain.scheduler.SchedulerConfig;
import com.enablix.scheduler.repo.SchedulerConfigRepository;

@Component
@ConditionalOnProperty(name="task.scheduler.enabled", matchIfMissing=true)
public class SpringTaskScheduler implements SchedulingConfigurer {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringTaskScheduler.class);
	
	@Autowired
	private ScheduledTaskExecutor taskExecutor;
	
	@Autowired
	private SchedulerConfigRepository schedulerConfigRepo;
	
	private ScheduledTaskRegistrar taskRegistrar;
	
	public void startScheduler(SchedulerConfig schedulerConfig) {
		
		LOGGER.info("Starting scheduler [name: {}, cron: {}]", 
				schedulerConfig.getName(), schedulerConfig.getCronExpression());
		
		ScheduledRunnable runnable = new ScheduledRunnable(schedulerConfig, taskExecutor);
		
		taskRegistrar.addCronTask(new CronTask(runnable, schedulerConfig.getCronExpression()));
		
	}
	
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		
		this.taskRegistrar = taskRegistrar;
		
		List<SchedulerConfig> schedulerConfigs = schedulerConfigRepo.findAll();
		
		for (SchedulerConfig config : schedulerConfigs) {
			startScheduler(config);
		}
		
	}
	
}
