package com.enablix.state.change.impl;

import com.enablix.state.change.definition.ActionConfiguration;
import com.enablix.state.change.definition.RecordingInstantiator;
import com.enablix.state.change.definition.StateChangeWorkflowDefinition;
import com.enablix.state.change.model.ActionInput;
import com.enablix.state.change.model.ActionResult;
import com.enablix.state.change.model.RefObject;
import com.enablix.state.change.model.StateChangeRecording;
import com.enablix.state.change.repo.StateChangeRecordingRepository;

public class StateChangeWorkflowDefinitionImpl<T extends RefObject, A extends StateChangeRecording<T>> 
			implements StateChangeWorkflowDefinition<T, A> {

	private String workflowName;
	private StateChangeRecordingRepository<T, A> repo;
	private RecordingInstantiator<T, A> instantiator;
	private ActionRegistry<T, A> actionRegistry;
	
	public StateChangeWorkflowDefinitionImpl(String workflowName, StateChangeRecordingRepository<T, A> repo,
			RecordingInstantiator<T, A> instantiator,
			ActionRegistry<T, A> actionRegistry) {
		super();
		this.workflowName = workflowName;
		this.repo = repo;
		this.instantiator = instantiator;
		this.actionRegistry = actionRegistry;
	}

	@Override
	public String workflowName() {
		return workflowName;
	}

	@Override
	public StateChangeRecordingRepository<T, A> workflowRepository() {
		return repo;
	}

	@Override
	public RecordingInstantiator<T, A> recordInstantiator() {
		return instantiator;
	}
	
	public void registerAction(String stateName, 
			ActionConfiguration<T, A, ? extends ActionInput, ?, ? extends ActionResult<T, ?>> action) {
		actionRegistry.addAllowedActionForState(stateName, action);
	}

	@Override
	public ActionConfiguration<T, A, ? extends ActionInput, ?, ? extends ActionResult<T, ?>> 
			getStateAction(String stateName, String actionName) {
		return actionRegistry.getActionConfig(stateName, actionName);
	}

}
