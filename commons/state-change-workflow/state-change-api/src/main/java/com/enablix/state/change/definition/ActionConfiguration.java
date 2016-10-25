package com.enablix.state.change.definition;

import com.enablix.state.change.NextStateBuilder;
import com.enablix.state.change.StateChangeAction;
import com.enablix.state.change.model.ActionInput;
import com.enablix.state.change.model.ActionResult;
import com.enablix.state.change.model.RefObject;

public interface ActionConfiguration<T extends RefObject, I extends ActionInput, V, R extends ActionResult<T, V>> {

	ActionDefinition getActionDefinition();
	
	StateChangeAction<T, I, V, R> getAction();
	
	NextStateBuilder<T, R, I> getNextStateBuilder();
	
}
