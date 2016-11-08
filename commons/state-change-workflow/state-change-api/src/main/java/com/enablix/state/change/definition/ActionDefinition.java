package com.enablix.state.change.definition;

import java.util.ArrayList;
import java.util.List;

public class ActionDefinition {

	private String actionName;
	
	private List<String> requiredPermissions;

	public ActionDefinition(String actionName) {
		super();
		this.actionName = actionName;
		this.requiredPermissions = new ArrayList<>();
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public List<String> getRequiredPermissions() {
		return requiredPermissions;
	}

	public void setRequiredPermissions(List<String> requiredPermissions) {
		this.requiredPermissions = requiredPermissions;
	}
	
	public void addRequiredPermission(String permission) {
		if (!requiredPermissions.contains(permission)) {
			requiredPermissions.add(permission);
		}
	}
	
}
