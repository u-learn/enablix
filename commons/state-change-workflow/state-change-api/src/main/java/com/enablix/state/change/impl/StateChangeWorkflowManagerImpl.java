package com.enablix.state.change.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.id.IdentityUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.state.change.ActionException;
import com.enablix.state.change.NextStateBuilder;
import com.enablix.state.change.StateChangeAction;
import com.enablix.state.change.StateChangeWorkflowManager;
import com.enablix.state.change.action.access.ActionAccessAuthorizer;
import com.enablix.state.change.definition.ActionConfiguration;
import com.enablix.state.change.definition.StateChangeWorkflowDefinition;
import com.enablix.state.change.definition.WorkflowDefinitionFactory;
import com.enablix.state.change.model.ActionData;
import com.enablix.state.change.model.ActionInput;
import com.enablix.state.change.model.ActionResult;
import com.enablix.state.change.model.ObjectState;
import com.enablix.state.change.model.RefObject;
import com.enablix.state.change.model.StateChangeRecording;
import com.enablix.state.change.repo.StateChangeRecordingRepository;

@Component
public class StateChangeWorkflowManagerImpl implements StateChangeWorkflowManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(StateChangeWorkflowManagerImpl.class);
	
	@Autowired
	private WorkflowDefinitionFactory factory;
	
	@SuppressWarnings({ "rawtypes" })
	@Override
	public <I extends ActionInput> void start(String workflowName, String actionName, 
			I actionInput) throws ActionException {
		
		StateChangeWorkflowDefinition<?, ?> wfDefinition = factory.getWorkflowDefinition(workflowName);
		
		ObjectState startState = ObjectState.startState();
		StateChangeRecording recording = wfDefinition.recordInstantiator().newInstance();
		recording.setCurrentState(startState);
		
		performAction(actionInput, wfDefinition, recording, actionName);
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private <I extends ActionInput> void performAction(I actionInput, 
			StateChangeWorkflowDefinition<?, ?> wfDefinition,
			StateChangeRecording recording, String actionName) throws ActionException {
		
		String workflowName = wfDefinition.workflowName();
		String currentStateName = recording.getCurrentState().getStateName();
		
		ActionConfiguration<?, ? extends StateChangeRecording<? extends RefObject>, ? extends ActionInput, ?, ?> actionConfig = 
				wfDefinition.getStateAction(currentStateName, actionName);
		if (actionConfig == null) {
			LOGGER.error("No action [{}] found for state [{}] in workflow [{}]", actionName, currentStateName, workflowName);
			throw new IllegalArgumentException("Action not found for current state");
		}
		
		// check permission of the current user to perform action
		ActionAccessAuthorizer authorizer = actionConfig.getAuthorizer();
		if (!authorizer.check(recording, actionConfig.getActionDefinition())) {
			LOGGER.error("Access denied for action [{}]", actionName);
			throw new AccessDeniedException("Acess denied for action: " + actionName);
		}
		
		StateChangeRecordingRepository repo = wfDefinition.workflowRepository();

		// execute action
		StateChangeAction action = actionConfig.getAction();
		ActionResult actionResult = action.execute(actionInput, recording.getObjectRef());
		
		RefObject updatedRefObject = actionResult.updatedRefObject();
		if (StringUtil.isEmpty(updatedRefObject.getIdentity())) {
			updatedRefObject.setIdentity(IdentityUtil.generateIdentity(updatedRefObject));
		}
		
		recording.setObjectRef(updatedRefObject);
		
		// Calculate next state
		NextStateBuilder nextStateBuilder = actionConfig.getNextStateBuilder();
		ObjectState currentState = recording.getCurrentState();
		ObjectState nextState = nextStateBuilder.nextState(currentState, 
								action.getActionName(), actionResult, actionInput);
		recording.setCurrentState(nextState);
		
		// Update action history
		String userId = ProcessContext.get().getUserId();
		String userName = ProcessContext.get().getUserDisplayName();
		
		recording.getActionHistory().getActions().add(
				new ActionData(action.getActionName(), currentState.getStateName(), nextState.getStateName(), 
								actionInput, actionResult.returnValue(), userId, userName, new Date()));
		
		repo.save(recording);
	}

	@Override
	public <I extends ActionInput> void executeAction(String workflowName, String refObjectIdentity, String action, I actionInput) throws ActionException {
		
		StateChangeWorkflowDefinition<?, ?> wfDefinition = factory.getWorkflowDefinition(workflowName);
		StateChangeRecordingRepository<?, ?> repo = wfDefinition.workflowRepository();

		StateChangeRecording<? extends RefObject> recording = repo.findByObjectRefIdentity(refObjectIdentity);
		if (recording == null) {
			LOGGER.error("No existing workflow found for reference object [{}]", refObjectIdentity);
			throw new IllegalArgumentException("No existing workflow found for reference object : " + refObjectIdentity);
		}
		
		performAction(actionInput, wfDefinition, recording, action);
	}
	
	@SuppressWarnings({ "unused" })
	private StateChangeWorkflowDefinition<?, ?> getWorkflowDefinition(String workflowName) {
		return factory.getWorkflowDefinition(workflowName);
	}

}
