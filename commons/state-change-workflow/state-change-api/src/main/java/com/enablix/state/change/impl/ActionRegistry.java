package com.enablix.state.change.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.enablix.state.change.definition.ActionConfiguration;
import com.enablix.state.change.definition.ActionDefinition;
import com.enablix.state.change.model.ActionInput;
import com.enablix.state.change.model.ActionResult;
import com.enablix.state.change.model.RefObject;

public class ActionRegistry<T extends RefObject> {

	private Map<String, Map<String, ActionConfiguration<T, ? extends ActionInput, ?, ? extends ActionResult<T, ?>>>> registry;
	
	private Map<String, List<ActionDefinition>> nextActions;
	
	public ActionRegistry() {
		registry = new HashMap<>();
		nextActions = new HashMap<>();
	}
	
	public <I extends ActionInput, V, R extends ActionResult<T, V>> 
	void addAllowedActionForState(String stateName, ActionConfiguration<T, I, V, R> action) {
		
		Map<String, ActionConfiguration<T, ? extends ActionInput, ?, ? extends ActionResult<T, ?>>> 
					actions = registry.get(stateName);
		
		if (actions == null) {
			actions = new HashMap<>();
			registry.put(stateName, actions);
		}
		
		actions.put(action.getAction().getActionName(), action);
		
		// add to next actions list
		List<ActionDefinition> actionList = nextActions.get(stateName);
		if (actionList == null) {
			actionList = new ArrayList<>();
			nextActions.put(stateName, actionList);
		}
		actionList.add(action.getActionDefinition());
		
	}
	
	public ActionConfiguration<T, ? extends ActionInput, ?, ? extends ActionResult<T, ?>>
			getActionConfig(String currentState, String nextAction) {
		
		ActionConfiguration<T, ? extends ActionInput, ?, ? extends ActionResult<T, ?>> action = null;
		
		Map<String, ActionConfiguration<T, ? extends ActionInput, ?, ? extends ActionResult<T, ?>>> 
					actions = registry.get(currentState);
		if (actions != null) {
			action = actions.get(nextAction);
		}
		
		return action;
	}
	
	public List<ActionDefinition> getNextAllowedActions(String stateName) {
		return nextActions.get(stateName);
	}
	
}
