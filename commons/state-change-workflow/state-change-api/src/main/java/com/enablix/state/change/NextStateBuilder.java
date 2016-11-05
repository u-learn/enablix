package com.enablix.state.change;

import com.enablix.state.change.model.ActionInput;
import com.enablix.state.change.model.ObjectState;

public interface NextStateBuilder<R, I extends ActionInput> {

	ObjectState nextState(ObjectState currentState, String actionName, R actionReturnValue, I actionInput);
	
}
