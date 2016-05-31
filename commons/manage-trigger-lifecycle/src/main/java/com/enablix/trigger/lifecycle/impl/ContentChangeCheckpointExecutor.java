package com.enablix.trigger.lifecycle.impl;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.template.service.TemplateManager;
import com.enablix.core.commons.xsdtopojo.ActionType;
import com.enablix.core.commons.xsdtopojo.CheckpointType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.commons.xsdtopojo.CorrelationActionType;
import com.enablix.core.commons.xsdtopojo.EmailActionType;
import com.enablix.core.domain.trigger.ContentChange;
import com.enablix.core.domain.trigger.LifecycleCheckpoint;
import com.enablix.trigger.lifecycle.CheckpointExecutor;
import com.enablix.trigger.lifecycle.action.CheckpointAction;
import com.enablix.trigger.lifecycle.action.CheckpointActionFactory;

@Component
public class ContentChangeCheckpointExecutor implements CheckpointExecutor<ContentChange> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContentChangeCheckpointExecutor.class);
	
	@Autowired
	private CheckpointActionFactory actionFactory;
	
	@Autowired
	private TemplateManager templateManager;
	
	@Override
	public void execute(LifecycleCheckpoint<ContentChange> checkpoint) {
		
		Date now = Calendar.getInstance().getTime();
		
		if (now.after(checkpoint.getScheduledExecDate())) {
			
			CheckpointType checkpointDef = checkpoint.getCheckpointDefinition();
			
			ContentTemplate template = templateManager.getTemplate(
					checkpoint.getTrigger().getTriggerItem().getTemplateId());
			
			CorrelationActionType correlationActionDef = checkpointDef.getActions().getCorrelation();
			List<EmailActionType> emailActionDefs = checkpointDef.getActions().getEmail();
			
			Set<ActionType> actionDefs = new TreeSet<>(new Comparator<ActionType>() {

				@Override
				public int compare(ActionType o1, ActionType o2) {
					final int compare = o1.getExecOrder().intValue() - o2.getExecOrder().intValue();
					return compare == 0 ? 0 : compare < 0 ? -1 : 1;
				}
			});
			
			if (correlationActionDef != null) {
				actionDefs.add(correlationActionDef);
			}
			
			if (emailActionDefs != null) {
				for (EmailActionType emailActDef : emailActionDefs) {
					actionDefs.add(emailActDef);
				}
			}
			
			for (ActionType actionDef : actionDefs) {
				CheckpointAction<ContentChange, ActionType> action = actionFactory.getCheckpointAction(actionDef);
				action.run(checkpoint, template, actionDef);
			}
			
		} else {
			LOGGER.info("Checkpoint execution time [{}] is after [{}]", checkpoint.getScheduledExecDate(), now);
		}
		
	}

}
