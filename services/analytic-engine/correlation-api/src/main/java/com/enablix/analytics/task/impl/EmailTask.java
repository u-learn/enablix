package com.enablix.analytics.task.impl;

import org.springframework.stereotype.Component;

import com.enablix.analytics.task.ContentChange;
import com.enablix.analytics.task.TaskChain;
import com.enablix.analytics.task.TriggerTask;

@Component
public class EmailTask implements TriggerTask<ContentChange> {

	@Override
	public void run(TaskChain<ContentChange> taskChain, ContentChange trigger) {
		// TODO Auto-generated method stub
		
		taskChain.doTask(trigger);
	}

}
