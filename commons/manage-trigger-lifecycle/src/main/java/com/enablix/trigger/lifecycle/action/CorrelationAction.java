package com.enablix.trigger.lifecycle.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.analytics.correlation.ItemCorrelationService;
import com.enablix.analytics.correlation.ItemUserCorrelationService;
import com.enablix.core.commons.xsdtopojo.ActionType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.commons.xsdtopojo.CorrelationActionType;
import com.enablix.core.domain.trigger.ContentChange;
import com.enablix.core.domain.trigger.LifecycleCheckpoint;

@Component
public class CorrelationAction implements CheckpointAction<ContentChange, CorrelationActionType> {

	@Autowired
	private ItemCorrelationService itemCorrService;
	
	@Autowired
	private ItemUserCorrelationService itemUserCorrService;
	
	@Override
	public void run(LifecycleCheckpoint<ContentChange> checkpoint, 
			ContentTemplate template, CorrelationActionType actionDef) {
		
		ContentChange trigger = checkpoint.getTrigger();
		
		itemCorrService.correlateItems(trigger.getTriggerItem());
		itemUserCorrService.correlateUsers(trigger.getTriggerItem());
		
	}

	@Override
	public boolean canHandle(ActionType action) {
		return action instanceof CorrelationActionType;
	}

}
