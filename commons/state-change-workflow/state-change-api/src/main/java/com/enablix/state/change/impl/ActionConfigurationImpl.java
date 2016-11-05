package com.enablix.state.change.impl;

import com.enablix.state.change.NextStateBuilder;
import com.enablix.state.change.StateChangeAction;
import com.enablix.state.change.action.access.ActionAccessAuthorizer;
import com.enablix.state.change.definition.ActionConfiguration;
import com.enablix.state.change.definition.ActionDefinition;
import com.enablix.state.change.model.ActionInput;
import com.enablix.state.change.model.ActionResult;
import com.enablix.state.change.model.RefObject;
import com.enablix.state.change.model.StateChangeRecording;

public class ActionConfigurationImpl<T extends RefObject, S extends StateChangeRecording<T>, I extends ActionInput, V, R extends ActionResult<T, V>> implements ActionConfiguration<T, S, I, V, R> {

	private ActionDefinition actionDefinition;
	private StateChangeAction<T, I, V, R> action;
	private NextStateBuilder<R, I> nextStateBuilder;
	private ActionAccessAuthorizer<T, S> authorizer;
	
	public ActionConfigurationImpl(ActionDefinition actionDefinition, StateChangeAction<T, I, V, R> action,
			NextStateBuilder<R, I> nextStateBuilder, ActionAccessAuthorizer<T, S> authorizer) {
		super();
		this.actionDefinition = actionDefinition;
		this.action = action;
		this.nextStateBuilder = nextStateBuilder;
		this.authorizer = authorizer;
	}

	@Override
	public ActionDefinition getActionDefinition() {
		return actionDefinition;
	}

	@Override
	public StateChangeAction<T, I, V, R> getAction() {
		return action;
	}

	@Override
	public NextStateBuilder<R, I> getNextStateBuilder() {
		return nextStateBuilder;
	}

	@Override
	public ActionAccessAuthorizer<T, S> getAuthorizer() {
		return authorizer;
	}

}
