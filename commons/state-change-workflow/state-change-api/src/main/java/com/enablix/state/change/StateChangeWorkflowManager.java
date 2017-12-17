package com.enablix.state.change;

import com.enablix.state.change.model.ActionInput;
import com.enablix.state.change.model.StateChangeRecording;

public interface StateChangeWorkflowManager {

	@SuppressWarnings("rawtypes")
	<I extends ActionInput> StateChangeRecording start(String workflowName, String actionName, I actionInput) throws ActionException;
	
	@SuppressWarnings("rawtypes")
	<I extends ActionInput> StateChangeRecording executeAction(String workflowName, 
			String refObjectIdentity, String action, I actionInput) throws ActionException;

}
