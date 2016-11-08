package com.enablix.state.change;

import com.enablix.state.change.model.ActionInput;
import com.enablix.state.change.model.RefObject;
import com.enablix.state.change.model.StateChangeRecording;

public interface ActionInterceptor<T extends RefObject, S extends StateChangeRecording<T>> {

	void onActionStart(String actionName, ActionInput actionIn, S recording);
	
	void onActionComplete(String actionName, ActionInput actionIn, S recording);
	
	void onError(String actionName, ActionInput actionIn, S recording, Throwable e);
	
	String[] actionsInterestedIn();
	
	String workflowName();
	
}
