package com.enablix.trigger.lifecycle.impl;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.enablix.core.commons.xsdtopojo.CheckpointType;
import com.enablix.core.commons.xsdtopojo.ExecTimeType;
import com.enablix.core.domain.trigger.Trigger;
import com.enablix.trigger.lifecycle.CheckpointExecutionTimeInterpreter;

@Component
public class RealtimeCheckpointExecTimeInterpreter implements CheckpointExecutionTimeInterpreter {

	@Override
	public <T extends Trigger> Date getCheckpointExecutionTime(CheckpointType checkpointDef, T trigger) {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.SECOND, -30); // subtract 30 to make sure it is executed right now even if there is some time comparison
		return now.getTime();
	}

	@Override
	public boolean canHandle(CheckpointType checkpointDef) {
		return checkpointDef.getExecutionTime().getType() == ExecTimeType.NOW;
	}

}
