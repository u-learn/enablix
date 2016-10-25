package com.enablix.state.change.impl;

import com.enablix.state.change.NextStateBuilder;
import com.enablix.state.change.StateChangeAction;
import com.enablix.state.change.definition.ActionConfiguration;
import com.enablix.state.change.definition.ActionDefinition;
import com.enablix.state.change.model.ActionInput;
import com.enablix.state.change.model.ActionResult;
import com.enablix.state.change.model.RefObject;

public class ActionConfigurationImpl<T extends RefObject, I extends ActionInput, V, R extends ActionResult<T, V>> implements ActionConfiguration<T, I, V, R> {

	private ActionDefinition actionDefinition;
	private StateChangeAction<T, I, V, R> action;
	private NextStateBuilder<T, R, I> nextStateBuilder;
	
	public ActionConfigurationImpl(ActionDefinition actionDefinition, StateChangeAction<T, I, V, R> action,
			NextStateBuilder<T, R, I> nextStateBuilder) {
		super();
		this.actionDefinition = actionDefinition;
		this.action = action;
		this.nextStateBuilder = nextStateBuilder;
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
	public NextStateBuilder<T, R, I> getNextStateBuilder() {
		return nextStateBuilder;
	}

}
