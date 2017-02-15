package com.enablix.trigger.lifecycle.action;

import com.enablix.core.commons.xsdtopojo.ActionType;
import com.enablix.core.domain.trigger.LifecycleCheckpoint;
import com.enablix.core.domain.trigger.Trigger;
import com.enablix.services.util.template.TemplateWrapper;

public interface CheckpointAction<T extends Trigger, A extends ActionType> {

	void run(LifecycleCheckpoint<T> checkpoint, TemplateWrapper template, A actionType);
	
	boolean canHandle(ActionType action);

}
