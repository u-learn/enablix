package com.enablix.state.change.action.access;

import com.enablix.state.change.definition.ActionDefinition;
import com.enablix.state.change.model.RefObject;
import com.enablix.state.change.model.StateChangeRecording;

public interface ActionAccessAuthorizer<T extends RefObject, R extends StateChangeRecording<T>> {

	boolean check(R recording, ActionDefinition action);
	
}
