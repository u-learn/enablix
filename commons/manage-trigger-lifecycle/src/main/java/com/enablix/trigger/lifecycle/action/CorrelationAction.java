package com.enablix.trigger.lifecycle.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.analytics.correlation.ItemCorrelationService;
import com.enablix.analytics.correlation.ItemUserCorrelationService;
import com.enablix.analytics.correlation.context.CorrelationContext;
import com.enablix.analytics.correlation.context.CorrelationContextBuilder;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ActionType;
import com.enablix.core.commons.xsdtopojo.CorrelationActionType;
import com.enablix.core.domain.trigger.ContentChange;
import com.enablix.core.domain.trigger.LifecycleCheckpoint;

@Component
public class CorrelationAction implements CheckpointAction<ContentChange, CorrelationActionType> {

	@Autowired
	private ItemCorrelationService itemCorrService;
	
	@Autowired
	private ItemUserCorrelationService itemUserCorrService;
	
	@Autowired
	private CorrelationContextBuilder contextBuilder;
	
	@Override
	public void run(LifecycleCheckpoint<ContentChange> checkpoint, 
			TemplateFacade template, CorrelationActionType actionDef) {
		
		ContentChange trigger = checkpoint.getTrigger();
		
		CorrelationContext context = contextBuilder.buildContext(trigger.getTriggerItem());
		
		itemCorrService.correlateItems(trigger.getTriggerItem(), context);
		itemUserCorrService.correlateUsers(trigger.getTriggerItem(), context);
		
	}

	@Override
	public boolean canHandle(ActionType action) {
		return action instanceof CorrelationActionType;
	}

}
