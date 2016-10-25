package com.enablix.state.change.model;

import com.enablix.core.domain.BaseDocumentEntity;

public abstract class StateChangeRecording<T extends RefObject> extends BaseDocumentEntity {

	private ObjectState currentState;
	
	private ActionHistory actionHistory;
	
	private T objectRef;
	
	public StateChangeRecording() {
		actionHistory = new ActionHistory();
	}

	public ObjectState getCurrentState() {
		return currentState;
	}

	public void setCurrentState(ObjectState currentState) {
		this.currentState = currentState;
	}

	public ActionHistory getActionHistory() {
		return actionHistory;
	}

	public void setActionHistory(ActionHistory actionHistory) {
		this.actionHistory = actionHistory;
	}

	public T getObjectRef() {
		return objectRef;
	}

	public void setObjectRef(T objectRef) {
		this.objectRef = objectRef;
	}
	
}
