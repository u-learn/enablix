package com.enablix.state.change.action.access.impl;

import org.springframework.stereotype.Component;

import com.enablix.core.security.SecurityUtil;
import com.enablix.state.change.action.access.ActionAccessAuthorizer;
import com.enablix.state.change.definition.ActionDefinition;
import com.enablix.state.change.model.RefObject;
import com.enablix.state.change.model.StateChangeRecording;

@Component
public class PermissionBasedAuthorizer<T extends RefObject, R extends StateChangeRecording<T>> implements ActionAccessAuthorizer<T, R> {

	@Override
	public boolean check(R recording, ActionDefinition action) {
		
		return SecurityUtil.currentUserHasAllPermission(
				action.getRequiredPermissions().toArray(new String[0]));
	}

}
