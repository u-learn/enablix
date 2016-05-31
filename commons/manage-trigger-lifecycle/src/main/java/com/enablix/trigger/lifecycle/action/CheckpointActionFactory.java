package com.enablix.trigger.lifecycle.action;

import com.enablix.core.commons.xsdtopojo.ActionType;
import com.enablix.core.domain.trigger.Trigger;

public interface CheckpointActionFactory {

	<T extends Trigger, A extends ActionType> CheckpointAction<T, A> getCheckpointAction(A actionDef);
	
}
