package com.enablix.state.change.impl;

import org.springframework.stereotype.Component;

import com.enablix.state.change.NextStateBuilder;
import com.enablix.state.change.model.ActionInput;
import com.enablix.state.change.model.ObjectState;
import com.enablix.state.change.model.RefObject;

@Component
public class NoChangeStateBuilder implements NextStateBuilder<RefObject, Object, ActionInput> {

	@Override
	public ObjectState nextState(ObjectState currentState, String actionName, Object actionReturnValue,
			ActionInput actionInput) {
		return currentState;
	}


}
