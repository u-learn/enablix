package com.enablix.trigger.lifecycle;

import java.util.Date;

import com.enablix.core.commons.xsdtopojo.CheckpointType;
import com.enablix.core.domain.trigger.Trigger;

public interface CheckpointExecutionTimeInterpreter {

	<T extends Trigger> Date getCheckpointExecutionTime(CheckpointType checkpointDef, T trigger);
	
	boolean canHandle(CheckpointType checkpointDef);
	
}
