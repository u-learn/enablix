package com.enablix.state.change.model;

import java.util.ArrayList;
import java.util.List;

public class ActionHistory {

	private List<ActionData<?, ?, ?>> actions;
	
	public ActionHistory() {
		this.actions = new ArrayList<>();
	}

	public List<ActionData<?, ?, ?>> getActions() {
		return actions;
	}

	public void setActions(List<ActionData<?, ?, ?>> actions) {
		this.actions = actions;
	}
	
}
