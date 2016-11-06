package com.enablix.state.change.model;

public class ObjectState {
	
	public static final String START_STATE = "NEW";

	private String stateName;
	
	public ObjectState(String stateName) {
		this.stateName = stateName;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public static ObjectState startState() {
		ObjectState startState = new ObjectState(START_STATE);
		return startState;
	}

}
