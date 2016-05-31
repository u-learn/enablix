package com.enablix.trigger.lifecycle.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.beans.SpringBackedAbstractFactory;
import com.enablix.core.commons.xsdtopojo.CheckpointType;
import com.enablix.core.commons.xsdtopojo.ExecTimeType;
import com.enablix.trigger.lifecycle.CheckpointExecTimeInterpreterFactory;
import com.enablix.trigger.lifecycle.CheckpointExecutionTimeInterpreter;

@Component
public class CheckpointExecTimeInterpreterFactoryImpl extends SpringBackedAbstractFactory<CheckpointExecutionTimeInterpreter> implements CheckpointExecTimeInterpreterFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(CheckpointExecTimeInterpreterFactoryImpl.class);
	
	@Override
	public CheckpointExecutionTimeInterpreter getInterpreter(CheckpointType checkpointDef) {
		
		for (CheckpointExecutionTimeInterpreter interpreter : registeredInstances()) {
			if (interpreter.canHandle(checkpointDef)) {
				return interpreter;
			}
		}
		
		ExecTimeType executionTimeType = checkpointDef.getExecutionTime().getType();
		LOGGER.error("No Interpreter found for checkpoint execution type [{}]", executionTimeType);
		throw new IllegalStateException("No Trigger Lifecycle Manager found for trigger [" + executionTimeType + "]");
		
	}

	@Override
	protected Class<CheckpointExecutionTimeInterpreter> lookupForType() {
		return CheckpointExecutionTimeInterpreter.class;
	}

}
