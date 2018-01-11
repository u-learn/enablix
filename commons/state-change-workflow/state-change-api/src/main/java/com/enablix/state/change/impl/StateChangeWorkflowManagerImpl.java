package com.enablix.state.change.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.id.IdentityUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentRecord;
import com.enablix.core.api.ContentRecordRef;
import com.enablix.core.domain.segment.DataSegmentAware;
import com.enablix.core.domain.segment.DataSegmentInfo;
import com.enablix.data.segment.view.DataSegmentInfoBuilder;
import com.enablix.state.change.ActionException;
import com.enablix.state.change.ActionInterceptor;
import com.enablix.state.change.NextStateBuilder;
import com.enablix.state.change.StateChangeAction;
import com.enablix.state.change.StateChangeConstants;
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
	
	@Autowired
	private ActionInterceptorRegistry actionInterceptorRegistry;
	
	@Autowired
	private DataSegmentInfoBuilder dsInfoBuilder;
	
	@SuppressWarnings({ "rawtypes" })
	@Override
	public <I extends ActionInput> StateChangeRecording start(String workflowName, String actionName, 
			I actionInput) throws ActionException {
		
		StateChangeWorkflowDefinition<?, ?> wfDefinition = factory.getWorkflowDefinition(workflowName);
		
		ObjectState startState = ObjectState.startState();
		StateChangeRecording recording = wfDefinition.recordInstantiator().newInstance();
		recording.setCurrentState(startState);
		
		performAction(actionInput, wfDefinition, recording, actionName);
		
		return recording;
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private StateChangeRecording performAction(ActionInput actionInput, 
			StateChangeWorkflowDefinition<?, ?> wfDefinition,
			StateChangeRecording recording, String actionName) throws ActionException {
		
		String workflowName = wfDefinition.workflowName();
		List<ActionInterceptor> interceptors = actionInterceptorRegistry.getInterceptors(workflowName, actionName);
		
		try {
			
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
			
			// execute action start interceptors
			executeActionStartInterceptors(interceptors, actionName, actionInput, recording);
			
			StateChangeRecordingRepository repo = wfDefinition.workflowRepository();
	
			// execute action
			StateChangeAction action = actionConfig.getAction();
			ActionResult actionResult = action.execute(actionInput, recording.getObjectRef(), recording);
			
			if (StateChangeConstants.ACTION_DISCARD.equals(actionName)) {
			
				// delete recording
				repo.delete(recording);
				
			} else {
			
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
				
				checkAndSetDataSegmentInfo(recording, actionInput);
				
				recording = (StateChangeRecording) repo.save(recording);
			}
			
			// execute action completed interceptors
			executeActionCompletedInterceptors(interceptors, actionName, actionInput, recording);
			
		} catch (Throwable t) {
			executeErrorInterceptors(interceptors, actionName, actionInput, recording, t);
			throw t;
		}
		
		return recording;
		
	}
	
	private void checkAndSetDataSegmentInfo(StateChangeRecording<?> recording, ActionInput actionInput) {
		
		if (recording instanceof DataSegmentAware) {
		
			DataSegmentAware dsAware = (DataSegmentAware) recording;
			
			if (actionInput instanceof ContentRecord) {
				
				DataSegmentInfo dsInfo = dsInfoBuilder.build((ContentRecord) actionInput);
				dsAware.setDataSegmentInfo(dsInfo);
				
			} else if (actionInput instanceof ContentRecordRef) {
				
				DataSegmentInfo dsInfo = dsInfoBuilder.build((ContentRecordRef) actionInput);
				dsAware.setDataSegmentInfo(dsInfo);
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void executeErrorInterceptors(List<ActionInterceptor> interceptors, 
			String actionName, ActionInput in, StateChangeRecording<?> recording, Throwable error) {
		for (ActionInterceptor interceptor : interceptors) {
			interceptor.onError(actionName, in, recording, error);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void executeActionCompletedInterceptors(List<ActionInterceptor> interceptors, 
					String actionName, ActionInput in, StateChangeRecording<?> recording) {
		for (ActionInterceptor interceptor : interceptors) {
			interceptor.onActionComplete(actionName, in, recording);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void executeActionStartInterceptors(List<ActionInterceptor> interceptors, 
					String actionName, ActionInput in, StateChangeRecording<?> recording) {
		for (ActionInterceptor interceptor : interceptors) {
			interceptor.onActionStart(actionName, in, recording);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public <I extends ActionInput> StateChangeRecording executeAction(String workflowName, 
			String refObjectIdentity, String action, I actionInput) throws ActionException {
		
		StateChangeWorkflowDefinition<?, ?> wfDefinition = factory.getWorkflowDefinition(workflowName);
		StateChangeRecordingRepository<?, ?> repo = wfDefinition.workflowRepository();

		StateChangeRecording<? extends RefObject> recording = repo.findByObjectRefIdentity(refObjectIdentity);
		if (recording == null) {
			LOGGER.error("No existing workflow found for reference object [{}]", refObjectIdentity);
			throw new IllegalArgumentException("No existing workflow found for reference object : " + refObjectIdentity);
		}
		
		performAction(actionInput, wfDefinition, recording, action);
		
		return recording;
	}
	
	@SuppressWarnings({ "unused" })
	private StateChangeWorkflowDefinition<?, ?> getWorkflowDefinition(String workflowName) {
		return factory.getWorkflowDefinition(workflowName);
	}

}
