package com.enablix.analytics.task.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.analytics.task.ContentChange;
import com.enablix.analytics.task.TaskChain;
import com.enablix.analytics.task.Trigger;
import com.enablix.analytics.task.TriggerTask;
import com.enablix.analytics.task.TriggerTaskFactory;

// TODO: this implementation needs to change and be configurable in xml - Task Model?

@Component
public class TriggerTaskFactoryImpl implements TriggerTaskFactory {

	@Autowired
	private CorrelationTask corrTask;
	
	@Autowired
	private EmailTask emailTask;
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Trigger> TaskChain<T> getTaskChain(T trigger) {
		
		if (trigger instanceof ContentChange) {
			
			@SuppressWarnings("rawtypes")
			TriggerTask[] tasks = new TriggerTask[2];
			
			tasks[0] = corrTask;
			tasks[1] = emailTask;
			
			TaskChain<ContentChange> chain = new TaskChainImpl<>(tasks);
			return (TaskChain<T>) chain;
		}
		
		return null;
	}

}
