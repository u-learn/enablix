package com.enablix.trigger.lifecycle;

import com.enablix.core.commons.xsdtopojo.CheckpointType;
import com.enablix.core.commons.xsdtopojo.ContentTriggerDefType;
import com.enablix.core.domain.trigger.LifecycleCheckpoint;
import com.enablix.core.domain.trigger.Trigger;

public interface LifecycleCheckpointBuilder {

	<T extends Trigger> LifecycleCheckpoint<T> build(
			ContentTriggerDefType triggerLifecycleRule, 
			CheckpointType checkpointDef, T trigger, 
			String triggerLifecycleId);
	
}
