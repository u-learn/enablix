package com.enablix.state.change;

import com.enablix.state.change.model.ActionInput;

public interface StateChangeWorkflowManager {

	<I extends ActionInput> void start(String workflowName, String actionName, I actionInput) throws ActionException;
	
	<I extends ActionInput> void executeAction(String workflowName, 
			String refObjectIdentity, String action, I actionInput) throws ActionException;

}
