package com.enablix.state.change.definition;

import com.enablix.state.change.NextStateBuilder;
import com.enablix.state.change.StateChangeAction;
import com.enablix.state.change.action.access.ActionAccessAuthorizer;
import com.enablix.state.change.model.ActionInput;
import com.enablix.state.change.model.ActionResult;
import com.enablix.state.change.model.RefObject;
import com.enablix.state.change.model.StateChangeRecording;

public interface ActionConfiguration<T extends RefObject, S extends StateChangeRecording<T>, I extends ActionInput, V, R extends ActionResult<T, V>> {

	ActionDefinition getActionDefinition();
	
	StateChangeAction<T, I, V, R> getAction();
	
	NextStateBuilder<R, I> getNextStateBuilder();
	
	ActionAccessAuthorizer<T, S> getAuthorizer();
	
}
