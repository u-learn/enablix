package com.enablix.state.change;

import com.enablix.state.change.model.ActionInput;
import com.enablix.state.change.model.ObjectState;
import com.enablix.state.change.model.RefObject;

public interface NextStateBuilder<T extends RefObject, R, I extends ActionInput> {

	ObjectState nextState(ObjectState currentState, String actionName, R actionReturnValue, I actionInput);
	
}
