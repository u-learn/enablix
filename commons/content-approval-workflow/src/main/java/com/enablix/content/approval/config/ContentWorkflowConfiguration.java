package com.enablix.content.approval.config;

import static com.enablix.content.approval.ContentApprovalConstants.STATE_DRAFT;
import static com.enablix.content.approval.ContentApprovalConstants.STATE_APPROVED;
import static com.enablix.content.approval.ContentApprovalConstants.STATE_PENDING_APPROVAL;
import static com.enablix.content.approval.ContentApprovalConstants.STATE_REJECTED;
import static com.enablix.content.approval.ContentApprovalConstants.STATE_WITHDRAWN;
import static com.enablix.content.approval.ContentApprovalConstants.WORKFLOW_NAME;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.enablix.commons.util.PermissionConstants;
import com.enablix.content.approval.action.ApproveAction;
import com.enablix.content.approval.action.EditAction;
import com.enablix.content.approval.action.RejectAction;
import com.enablix.content.approval.action.SaveDraftAction;
import com.enablix.content.approval.action.SubmitAction;
import com.enablix.content.approval.action.WithdrawAction;
import com.enablix.content.approval.model.ContentApproval;
import com.enablix.content.approval.model.ContentDetail;
import com.enablix.content.approval.repo.ContentApprovalRepository;
import com.enablix.state.change.action.access.impl.CreatedByAuthorizer;
import com.enablix.state.change.action.access.impl.OrAuthorizer;
import com.enablix.state.change.action.access.impl.PermissionBasedAuthorizer;
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
	
	@SuppressWarnings("rawtypes")
	@Autowired
	private PermissionBasedAuthorizer permissionAuth;
	
	@SuppressWarnings("rawtypes")
	@Autowired
	private CreatedByAuthorizer createdByAuth;
	
	
	@Bean
	public StateChangeWorkflowDefinition<ContentDetail, ContentApproval> contentApprovalWFDefinition() {
		
		StateChangeWorkflowDefinitionImpl<ContentDetail, ContentApproval> def = 
				new StateChangeWorkflowDefinitionImpl<>(WORKFLOW_NAME, repo, 
						new ContentApprovalInstantiator(), contentActionRegistry());
		
		def.registerAction(ObjectState.START_STATE, saveDraftContentActionConfig());
		def.registerAction(ObjectState.START_STATE, contentApproveActionConfig());
		def.registerAction(ObjectState.START_STATE, contentSubmitActionConfig());
		
		def.registerAction(STATE_DRAFT, contentEditActionConfig());
		def.registerAction(STATE_DRAFT, contentSubmitActionConfig());
		def.registerAction(STATE_DRAFT, contentApproveActionConfig());
		
		def.registerAction(STATE_PENDING_APPROVAL, contentEditActionConfig());
		def.registerAction(STATE_PENDING_APPROVAL, contentApproveActionConfig());
		def.registerAction(STATE_PENDING_APPROVAL, contentRejectActionConfig());
		def.registerAction(STATE_PENDING_APPROVAL, contentWithdrawActionConfig());
		
		return def;
	}

	@Bean
	public SaveDraftAction saveDraftContentAction() {
		return new SaveDraftAction();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Bean
	public ActionConfigurationImpl<ContentDetail, ContentApproval, ContentDetail, Boolean, GenericActionResult<ContentDetail, Boolean>> 
			saveDraftContentActionConfig() {
		
		SaveDraftAction action = saveDraftContentAction();
		ActionDefinition actionDef = new ActionDefinition(action.getActionName());
		
		SimpleNextStateBuilder<Object, ActionInput> nextStateBuilder = simpleContentNextStateBuilder();
		nextStateBuilder.addNextStateConfig(ObjectState.START_STATE, action.getActionName(), STATE_DRAFT);
		
		ActionConfigurationImpl<ContentDetail, ContentApproval, ContentDetail, Boolean, GenericActionResult<ContentDetail, Boolean>> 
			saveDraftActionConfig = new ActionConfigurationImpl(actionDef, action, nextStateBuilder, permissionAuth);
		
		return saveDraftActionConfig;
	}
	
	@Bean
	public SubmitAction contentSubmitAction() {
		return new SubmitAction();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Bean
	public ActionConfigurationImpl<ContentDetail, ContentApproval, ContentDetail, Boolean, GenericActionResult<ContentDetail, Boolean>> 
			contentSubmitActionConfig() {
		
		SubmitAction action = contentSubmitAction();
		ActionDefinition actionDef = new ActionDefinition(action.getActionName());
		
		SimpleNextStateBuilder<Object, ActionInput> nextStateBuilder = simpleContentNextStateBuilder();
		nextStateBuilder.addNextStateConfig(ObjectState.START_STATE, action.getActionName(), STATE_PENDING_APPROVAL);
		nextStateBuilder.addNextStateConfig(STATE_DRAFT, action.getActionName(), STATE_PENDING_APPROVAL);
		
		ActionConfigurationImpl<ContentDetail, ContentApproval, ContentDetail, Boolean, GenericActionResult<ContentDetail, Boolean>> 
			submitActionConfig = new ActionConfigurationImpl(actionDef, action, nextStateBuilder, permissionAuth);
		
		return submitActionConfig;
	}
	
	@Bean
	public EditAction contentEditAction() {
		return new EditAction();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Bean
	public ActionConfigurationImpl<ContentDetail, ContentApproval, ContentDetail, Boolean, GenericActionResult<ContentDetail, Boolean>> 
			contentEditActionConfig() {
		
		EditAction docEditAction = contentEditAction();
		ActionDefinition actionDef = new ActionDefinition(docEditAction.getActionName());
		actionDef.addRequiredPermission(PermissionConstants.PERMISSION_MNG_CONTENT_REQUEST);
		
		ActionConfigurationImpl<ContentDetail, ContentApproval, ContentDetail, Boolean, GenericActionResult<ContentDetail, Boolean>> 
			editActionConfig = new ActionConfigurationImpl(actionDef, docEditAction, noChangeStateBuilder, 
								new OrAuthorizer<ContentDetail, ContentApproval>(permissionAuth, createdByAuth));
		
		return editActionConfig;
	}
	
	@Bean
	public ApproveAction contentApproveAction() {
		return new ApproveAction();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Bean
	public ActionConfigurationImpl<ContentDetail, ContentApproval, ContentDetail, Boolean, GenericActionResult<ContentDetail, Boolean>> 
			contentApproveActionConfig() {
		
		ApproveAction action = contentApproveAction();
		ActionDefinition actionDef = new ActionDefinition(action.getActionName());
		actionDef.addRequiredPermission(PermissionConstants.PERMISSION_MNG_CONTENT_REQUEST);
		
		SimpleNextStateBuilder<Object, ActionInput> nextStateBuilder = simpleContentNextStateBuilder();
		nextStateBuilder.addNextStateConfig(ObjectState.START_STATE, action.getActionName(), STATE_APPROVED);
		nextStateBuilder.addNextStateConfig(STATE_DRAFT, action.getActionName(), STATE_APPROVED);
		nextStateBuilder.addNextStateConfig(STATE_PENDING_APPROVAL, action.getActionName(), STATE_APPROVED);
		
		
		ActionConfigurationImpl<ContentDetail, ContentApproval, ContentDetail, Boolean, GenericActionResult<ContentDetail, Boolean>> 
			pubActionConfig = new ActionConfigurationImpl(actionDef, action, nextStateBuilder, permissionAuth);
		
		return pubActionConfig;
	}
	
	@Bean
	public RejectAction contentRejectAction() {
		return new RejectAction();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Bean
	public ActionConfigurationImpl<ContentDetail, ContentApproval, ContentDetail, Boolean, GenericActionResult<ContentDetail, Boolean>> 
			contentRejectActionConfig() {
		
		RejectAction action = contentRejectAction();
		ActionDefinition actionDef = new ActionDefinition(action.getActionName());
		actionDef.addRequiredPermission(PermissionConstants.PERMISSION_MNG_CONTENT_REQUEST);
		
		SimpleNextStateBuilder<Object, ActionInput> nextStateBuilder = simpleContentNextStateBuilder();
		nextStateBuilder.addNextStateConfig(STATE_PENDING_APPROVAL, action.getActionName(), STATE_REJECTED);
		
		ActionConfigurationImpl<ContentDetail, ContentApproval, ContentDetail, Boolean, GenericActionResult<ContentDetail, Boolean>> 
			actionConfig = new ActionConfigurationImpl(actionDef, action, nextStateBuilder, permissionAuth);
		
		return actionConfig;
	}
	
	@Bean
	public WithdrawAction contentWithdrawAction() {
		return new WithdrawAction();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Bean
	public ActionConfigurationImpl<ContentDetail, ContentApproval, ContentDetail, Boolean, GenericActionResult<ContentDetail, Boolean>> 
			contentWithdrawActionConfig() {
		
		WithdrawAction action = contentWithdrawAction();
		ActionDefinition actionDef = new ActionDefinition(action.getActionName());
		
		SimpleNextStateBuilder<Object, ActionInput> nextStateBuilder = simpleContentNextStateBuilder();
		nextStateBuilder.addNextStateConfig(STATE_PENDING_APPROVAL, action.getActionName(), STATE_WITHDRAWN);
		
		ActionConfigurationImpl<ContentDetail, ContentApproval, ContentDetail, Boolean, GenericActionResult<ContentDetail, Boolean>> 
			actionConfig = new ActionConfigurationImpl(actionDef, action, nextStateBuilder, createdByAuth);
		
		return actionConfig;
	}
	
	@Bean
	public ActionRegistry<ContentDetail, ContentApproval> contentActionRegistry() {
		return new ActionRegistry<>();
	}

	@Bean
	public SimpleNextStateBuilder<Object, ActionInput> simpleContentNextStateBuilder() {
		return new SimpleNextStateBuilder<>();
	}
	
	
}
