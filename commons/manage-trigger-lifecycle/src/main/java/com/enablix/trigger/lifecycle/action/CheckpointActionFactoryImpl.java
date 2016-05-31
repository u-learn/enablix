package com.enablix.trigger.lifecycle.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.beans.SpringBackedAbstractFactory;
import com.enablix.core.commons.xsdtopojo.ActionType;
import com.enablix.core.domain.trigger.Trigger;

@SuppressWarnings("rawtypes")
@Component
public class CheckpointActionFactoryImpl extends SpringBackedAbstractFactory<CheckpointAction> implements CheckpointActionFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(CheckpointActionFactoryImpl.class);

	@Override
	protected Class<CheckpointAction> lookupForType() {
		return CheckpointAction.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Trigger, A extends ActionType> CheckpointAction<T, A> getCheckpointAction(A actionDef) {
		
		for (CheckpointAction<?, ?> action : registeredInstances()) {
			if (action.canHandle(actionDef)) {
				return (CheckpointAction<T, A>) action;
			}
		}
		
		LOGGER.error("No action handler found for checkpoint action def [{}]", actionDef.getClass().getName());
		throw new IllegalStateException("No action handler found for checkpoint action def [" 
												+ actionDef.getClass().getName() + "]");
	}

}
