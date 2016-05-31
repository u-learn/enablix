package com.enablix.trigger.lifecycle.action;

import com.enablix.core.commons.xsdtopojo.ActionType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.domain.trigger.LifecycleCheckpoint;
import com.enablix.core.domain.trigger.Trigger;

public interface CheckpointAction<T extends Trigger, A extends ActionType> {

	void run(LifecycleCheckpoint<T> checkpoint, ContentTemplate template, A actionType);
	
	boolean canHandle(ActionType action);

}
