package com.enablix.state.change.action.access.impl;

import com.enablix.state.change.action.access.ActionAccessAuthorizer;
import com.enablix.state.change.definition.ActionDefinition;
import com.enablix.state.change.model.RefObject;
import com.enablix.state.change.model.StateChangeRecording;

public class NotAuthorizer<T extends RefObject, R extends StateChangeRecording<T>> implements ActionAccessAuthorizer<T, R> {

	private ActionAccessAuthorizer<T, R> authorizer;
	
	public NotAuthorizer(ActionAccessAuthorizer<T, R> authorizer) {
		this.authorizer = authorizer;
	}
	
	@Override
	public boolean check(R recording, ActionDefinition action) {
		return !authorizer.check(recording, action);
	}

	
}
