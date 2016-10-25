package com.enablix.state.change.model;

public class SimpleActionInput extends RefObject implements ActionInput {

	private String notes;
	
	public SimpleActionInput() {
		// default constructor
	}
	
	public SimpleActionInput(String identity) {
		setIdentity(identity);
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
}
