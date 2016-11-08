package com.enablix.state.change.definition;

import com.enablix.state.change.model.ActionInput;
import com.enablix.state.change.model.ActionResult;
import com.enablix.state.change.model.RefObject;
import com.enablix.state.change.model.StateChangeRecording;
import com.enablix.state.change.repo.StateChangeRecordingRepository;

public interface StateChangeWorkflowDefinition<T extends RefObject, A extends StateChangeRecording<T>> {

	String workflowName();
	
	ActionConfiguration<T, A, ? extends ActionInput, ?, ? extends ActionResult<T, ?>> 
		getStateAction(String stateName, String actionName);
	
	StateChangeRecordingRepository<T, A> workflowRepository();
	
	RecordingInstantiator<T, A> recordInstantiator();
	
	
	
}
