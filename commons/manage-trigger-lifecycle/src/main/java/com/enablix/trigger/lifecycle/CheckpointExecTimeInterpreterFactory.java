package com.enablix.trigger.lifecycle;

import com.enablix.core.commons.xsdtopojo.CheckpointType;

public interface CheckpointExecTimeInterpreterFactory {

	CheckpointExecutionTimeInterpreter getInterpreter(CheckpointType checkpointDef);
	
}
