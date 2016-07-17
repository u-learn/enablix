package com.enablix.trigger.lifecycle.impl;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.enablix.core.commons.xsdtopojo.CheckpointType;
import com.enablix.core.commons.xsdtopojo.ExecTimeType;
import com.enablix.core.commons.xsdtopojo.LaterExecutionTimeType;
import com.enablix.core.domain.trigger.Trigger;
import com.enablix.trigger.lifecycle.CheckpointExecutionTimeInterpreter;

@Component
public class LaterCheckpointExecTimeInterpreter implements CheckpointExecutionTimeInterpreter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LaterCheckpointExecTimeInterpreter.class);
	
	@Override
	public <T extends Trigger> Date getCheckpointExecutionTime(CheckpointType checkpointDef, T trigger) {
		
		Calendar refDate = Calendar.getInstance();
		refDate.setTime(trigger.getTriggerTime());
		
		LaterExecutionTimeType laterTimeDef = checkpointDef.getExecutionTime().getAfter();
		
		if (laterTimeDef == null) {
		
			LOGGER.error("Checkpoint later execution time not defined");
			return null;
			
		} else {
			
			switch (laterTimeDef.getTimeUnit()) {
				case MIN:
					refDate.add(Calendar.MINUTE, laterTimeDef.getTimeValue().intValue());
					break;
	
				case DAY:
					refDate.add(Calendar.DAY_OF_YEAR, laterTimeDef.getTimeValue().intValue());
					break;
	
				case WEEK:
					refDate.add(Calendar.WEEK_OF_YEAR, laterTimeDef.getTimeValue().intValue());
					break;
					
				default:
					break;
			}

		}
		
		
		return refDate.getTime();
	}

	@Override
	public boolean canHandle(CheckpointType checkpointDef) {
		return checkpointDef.getExecutionTime().getType() == ExecTimeType.LATER;
	}

}
