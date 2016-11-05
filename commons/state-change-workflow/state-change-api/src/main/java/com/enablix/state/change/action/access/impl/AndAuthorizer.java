package com.enablix.state.change.action.access.impl;

import com.enablix.state.change.action.access.ActionAccessAuthorizer;
import com.enablix.state.change.definition.ActionDefinition;
import com.enablix.state.change.model.RefObject;
import com.enablix.state.change.model.StateChangeRecording;

public class AndAuthorizer<T extends RefObject, R extends StateChangeRecording<T>> implements ActionAccessAuthorizer<T, R> {

	private ActionAccessAuthorizer<T, R> authorizers[];
	
	@SuppressWarnings("unchecked")
	public AndAuthorizer(ActionAccessAuthorizer<T, R>... authorizers) {
		this.authorizers = authorizers;
	}
	
	@Override
	public boolean check(R recording, ActionDefinition action) {
		
		for (ActionAccessAuthorizer<T, R> authorizer : authorizers) {
		
			if (!authorizer.check(recording, action)) {
				return false;
			}
		}
		
		return true;
	}

	
}
