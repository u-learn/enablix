package com.enablix.analytics.task.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.analytics.correlation.ItemCorrelationService;
import com.enablix.analytics.correlation.ItemUserCorrelationService;
import com.enablix.analytics.task.ContentChange;
import com.enablix.analytics.task.TaskChain;
import com.enablix.analytics.task.TriggerTask;

@Component
public class CorrelationTask implements TriggerTask<ContentChange> {

	@Autowired
	private ItemCorrelationService itemCorrService;
	
	@Autowired
	private ItemUserCorrelationService itemUserCorrService;
	
	@Override
	public void run(TaskChain<ContentChange> taskChain, ContentChange trigger) {
		
		itemCorrService.correlateItems(trigger.getTriggerItem());
		
		itemUserCorrService.correlateUsers(trigger.getTriggerItem());
		
		taskChain.doTask(trigger);
	}

}
