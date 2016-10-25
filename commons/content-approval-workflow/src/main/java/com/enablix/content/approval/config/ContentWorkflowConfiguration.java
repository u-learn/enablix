package com.enablix.content.approval.config;

import static com.enablix.content.approval.ContentApprovalConstants.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.enablix.content.approval.action.ApproveAction;
import com.enablix.content.approval.action.EditAction;
import com.enablix.content.approval.action.RejectAction;
import com.enablix.content.approval.action.SubmitAction;
import com.enablix.content.approval.model.ContentApproval;
import com.enablix.content.approval.model.ContentDetail;
import com.enablix.content.approval.repo.ContentApprovalRepository;
import com.enablix.state.change.definition.ActionDefinition;
import com.enablix.state.change.definition.StateChangeWorkflowDefinition;
import com.enablix.state.change.impl.ActionConfigurationImpl;
import com.enablix.state.change.impl.ActionRegistry;
import com.enablix.state.change.impl.NoChangeStateBuilder;
import com.enablix.state.change.impl.SimpleNextStateBuilder;
import com.enablix.state.change.impl.StateChangeWorkflowDefinitionImpl;
import com.enablix.state.change.model.ActionInput;
import com.enablix.state.change.model.GenericActionResult;
import com.enablix.state.change.model.ObjectState;

@Configuration
public class ContentWorkflowConfiguration {

	@Autowired
	private ContentApprovalRepository repo;
	
	@Autowired
	private NoChangeStateBuilder noChangeStateBuilder;
	
	@Bean
	public StateChangeWorkflowDefinition<ContentDetail, ContentApproval> contentApprovalWFDefinition() {
		
		StateChangeWorkflowDefinitionImpl<ContentDetail, ContentApproval> def = 
				new StateChangeWorkflowDefinitionImpl<>(WORKFLOW_NAME, repo, 
						new ContentApprovalInitiator(), contentActionRegistry());
		
		def.registerAction(ObjectState.START_STATE, contentApproveActionConfig());
		def.registerAction(ObjectState.START_STATE, contentSubmitActionConfig());
		
		def.registerAction(STATE_PENDING_APPROVAL, contentEditActionConfig());
		def.registerAction(STATE_PENDING_APPROVAL, contentApproveActionConfig());
		def.registerAction(STATE_PENDING_APPROVAL, contentRejectActionConfig());
		
		return def;
	}

	@Bean
	public SubmitAction contentSubmitAction() {
		return new SubmitAction();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Bean
	public ActionConfigurationImpl<ContentDetail, ContentDetail, Boolean, GenericActionResult<ContentDetail, Boolean>> 
			contentSubmitActionConfig() {
		
		SubmitAction action = contentSubmitAction();
		ActionDefinition actionDef = new ActionDefinition(action.getActionName());
		
		SimpleNextStateBuilder<ContentDetail, Object, ActionInput> nextStateBuilder = simpleContentNextStateBuilder();
		nextStateBuilder.addNextStateConfig(ObjectState.START_STATE, action.getActionName(), STATE_PENDING_APPROVAL);
		
		ActionConfigurationImpl<ContentDetail, ContentDetail, Boolean, GenericActionResult<ContentDetail, Boolean>> 
			orphanActionConfig = new ActionConfigurationImpl(actionDef, action, nextStateBuilder);
		
		return orphanActionConfig;
	}
	
	@Bean
	public EditAction contentEditAction() {
		return new EditAction();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Bean
	public ActionConfigurationImpl<ContentDetail, ContentDetail, Boolean, GenericActionResult<ContentDetail, Boolean>> 
			contentEditActionConfig() {
		
		EditAction docEditAction = contentEditAction();
		ActionDefinition actionDef = new ActionDefinition(docEditAction.getActionName());
		
		ActionConfigurationImpl<ContentDetail, ContentDetail, Boolean, GenericActionResult<ContentDetail, Boolean>> 
			editActionConfig = new ActionConfigurationImpl(actionDef, docEditAction, noChangeStateBuilder);
		
		return editActionConfig;
	}
	
	@Bean
	public ApproveAction contentApproveAction() {
		return new ApproveAction();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Bean
	public ActionConfigurationImpl<ContentDetail, ContentDetail, Boolean, GenericActionResult<ContentDetail, Boolean>> 
			contentApproveActionConfig() {
		
		ApproveAction action = contentApproveAction();
		ActionDefinition actionDef = new ActionDefinition(action.getActionName());
		
		SimpleNextStateBuilder<ContentDetail, Object, ActionInput> nextStateBuilder = simpleContentNextStateBuilder();
		nextStateBuilder.addNextStateConfig(ObjectState.START_STATE, action.getActionName(), STATE_APPROVED);
		nextStateBuilder.addNextStateConfig(STATE_PENDING_APPROVAL, action.getActionName(), STATE_APPROVED);
		
		ActionConfigurationImpl<ContentDetail, ContentDetail, Boolean, GenericActionResult<ContentDetail, Boolean>> 
			pubActionConfig = new ActionConfigurationImpl(actionDef, action, nextStateBuilder);
		
		return pubActionConfig;
	}
	
	@Bean
	public RejectAction contentRejectAction() {
		return new RejectAction();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Bean
	public ActionConfigurationImpl<ContentDetail, ContentDetail, Boolean, GenericActionResult<ContentDetail, Boolean>> 
			contentRejectActionConfig() {
		
		RejectAction action = contentRejectAction();
		ActionDefinition actionDef = new ActionDefinition(action.getActionName());
		
		SimpleNextStateBuilder<ContentDetail, Object, ActionInput> nextStateBuilder = simpleContentNextStateBuilder();
		nextStateBuilder.addNextStateConfig(STATE_PENDING_APPROVAL, action.getActionName(), STATE_REJECTED);
		
		ActionConfigurationImpl<ContentDetail, ContentDetail, Boolean, GenericActionResult<ContentDetail, Boolean>> 
			actionConfig = new ActionConfigurationImpl(actionDef, action, nextStateBuilder);
		
		return actionConfig;
	}
	
	@Bean
	public ActionRegistry<ContentDetail> contentActionRegistry() {
		return new ActionRegistry<>();
	}

	@Bean
	public SimpleNextStateBuilder<ContentDetail, Object, ActionInput> simpleContentNextStateBuilder() {
		
		SimpleNextStateBuilder<ContentDetail, Object, ActionInput> builder = 
				new SimpleNextStateBuilder<>(contentActionRegistry());
		
		return builder;
		
	}
	
	
}
