package com.enablix.scheduler;

import com.enablix.core.domain.scheduler.SchedulerConfig;

public class ScheduledRunnable implements Runnable {

	private SchedulerConfig schedulerConfig;
	
	private ScheduledTaskExecutor scheduledTaskExecutor;
	
	public ScheduledRunnable(SchedulerConfig schedulerConfig, ScheduledTaskExecutor scheduledTaskExecutor) {
		super();
		this.schedulerConfig = schedulerConfig;
		this.scheduledTaskExecutor = scheduledTaskExecutor;
	}

	@Override
	public void run() {
		scheduledTaskExecutor.executeScheduledTasks(schedulerConfig);
	}

}
