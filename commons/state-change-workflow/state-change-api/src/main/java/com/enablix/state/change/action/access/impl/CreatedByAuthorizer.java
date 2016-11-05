package com.enablix.state.change.action.access.impl;

import org.springframework.stereotype.Component;

import com.enablix.commons.util.process.ProcessContext;
import com.enablix.state.change.action.access.ActionAccessAuthorizer;
import com.enablix.state.change.definition.ActionDefinition;
import com.enablix.state.change.model.RefObject;
import com.enablix.state.change.model.StateChangeRecording;

@Component
public class CreatedByAuthorizer<T extends RefObject, R extends StateChangeRecording<T>> implements ActionAccessAuthorizer<T, R> {

	@Override
	public boolean check(R recording, ActionDefinition action) {
		return ProcessContext.get().getUserId().equals(recording.getCreatedBy());
	}

}
