package com.enablix.trigger.lifecycle.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.core.commons.xsdtopojo.CheckpointType;
import com.enablix.core.commons.xsdtopojo.ContentTriggerDefType;
import com.enablix.core.domain.trigger.LifecycleCheckpoint;
import com.enablix.core.domain.trigger.Trigger;
import com.enablix.trigger.lifecycle.CheckpointExecTimeInterpreterFactory;
import com.enablix.trigger.lifecycle.CheckpointExecutionTimeInterpreter;
import com.enablix.trigger.lifecycle.LifecycleCheckpointBuilder;

@Component
public class LifecycleCheckpointBuilderImpl implements LifecycleCheckpointBuilder {

	@Autowired
	private CheckpointExecTimeInterpreterFactory execTimeInterpreterFactory;
	
	@Override
	public <T extends Trigger> LifecycleCheckpoint<T> build(ContentTriggerDefType triggerLifecycleRule,
			CheckpointType checkpointDef, T trigger, String triggerLifecycleId) {
		
		CheckpointExecutionTimeInterpreter execTimeInterpreter = execTimeInterpreterFactory.getInterpreter(checkpointDef);
		Date toBeExecutedOn = execTimeInterpreter.getCheckpointExecutionTime(checkpointDef, trigger);
		
		LifecycleCheckpoint<T> checkpoint = new LifecycleCheckpoint<>();
		checkpoint.setCheckpointDefinition(checkpointDef);
		checkpoint.setScheduledExecDate(toBeExecutedOn);
		checkpoint.setTrigger(trigger);
		checkpoint.setTriggerLifecycleRuleId(triggerLifecycleRule.getId());
		checkpoint.setTriggerLifecycleId(triggerLifecycleId);
		
		return checkpoint;
	}

}
