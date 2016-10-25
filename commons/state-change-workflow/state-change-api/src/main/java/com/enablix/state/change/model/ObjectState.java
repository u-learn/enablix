package com.enablix.state.change.model;

import java.util.ArrayList;
import java.util.List;

import com.enablix.state.change.definition.ActionDefinition;

public class ObjectState {
	
	public static final String START_STATE = "NEW";

	private String stateName;
	
	private List<ActionDefinition> nextActions;
	
	public ObjectState(String stateName) {
		this.stateName = stateName;
		this.nextActions = new ArrayList<>();
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public List<ActionDefinition> getNextActions() {
		return nextActions;
	}

	public void setNextActions(List<ActionDefinition> nextActions) {
		this.nextActions = nextActions;
	}
	
	
	public void addNextAction(ActionDefinition actionDef) {
		nextActions.add(actionDef);
	}
	
	public static ObjectState startState() {
		ObjectState startState = new ObjectState(START_STATE);
		return startState;
	}

}
