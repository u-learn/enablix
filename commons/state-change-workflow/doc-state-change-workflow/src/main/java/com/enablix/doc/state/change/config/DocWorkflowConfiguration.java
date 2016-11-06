package com.enablix.doc.state.change.config;

import static com.enablix.doc.state.change.DocStateChangeConstants.STATE_PENDING;
import static com.enablix.doc.state.change.DocStateChangeConstants.STATE_PUBLISHED;
import static com.enablix.doc.state.change.DocStateChangeConstants.STATE_REJECTED;
import static com.enablix.doc.state.change.DocStateChangeConstants.WORKFLOW_NAME;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.enablix.doc.state.change.DocPublishingInstantiator;
import com.enablix.doc.state.change.action.EditAction;
import com.enablix.doc.state.change.action.OrphanUploadAction;
import com.enablix.doc.state.change.action.PublishAction;
import com.enablix.doc.state.change.action.RejectAction;
import com.enablix.doc.state.change.model.DocActionResult;
import com.enablix.doc.state.change.model.DocInfo;
import com.enablix.doc.state.change.model.DocPublishing;
import com.enablix.doc.state.change.repo.DocPublishingRepository;
import com.enablix.state.change.action.access.impl.PermissionBasedAuthorizer;
import com.enablix.state.change.definition.ActionDefinition;
import com.enablix.state.change.definition.StateChangeWorkflowDefinition;
import com.enablix.state.change.impl.ActionConfigurationImpl;
import com.enablix.state.change.impl.ActionRegistry;
import com.enablix.state.change.impl.NoChangeStateBuilder;
import com.enablix.state.change.impl.SimpleNextStateBuilder;
import com.enablix.state.change.impl.StateChangeWorkflowDefinitionImpl;
import com.enablix.state.change.model.ActionInput;
import com.enablix.state.change.model.ObjectState;

@Configuration
public class DocWorkflowConfiguration {

	@Autowired
	private DocPublishingRepository repo;
	
	@Autowired
	private NoChangeStateBuilder noChangeStateBuilder;
	
	@SuppressWarnings("rawtypes")
	@Autowired
	private PermissionBasedAuthorizer permissionAuth;
	
	@Bean
	public StateChangeWorkflowDefinition<DocInfo, DocPublishing> docStateChangeWFDefinition() {
		
		StateChangeWorkflowDefinitionImpl<DocInfo, DocPublishing> def = 
				new StateChangeWorkflowDefinitionImpl<>(WORKFLOW_NAME, repo, 
						new DocPublishingInstantiator(), docActionRegistry());
		
		def.registerAction(ObjectState.START_STATE, docPublishActionConfig());
		def.registerAction(ObjectState.START_STATE, docOrphanUploadActionConfig());
		
		def.registerAction(STATE_PENDING, docEditActionConfig());
		def.registerAction(STATE_PENDING, docPublishActionConfig());
		def.registerAction(STATE_PENDING, docRejectActionConfig());
		
		return def;
	}

	@Bean
	public OrphanUploadAction docOrphanUploadAction() {
		return new OrphanUploadAction();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Bean
	public ActionConfigurationImpl<DocInfo, DocPublishing, DocInfo, Boolean, DocActionResult<Boolean>> docOrphanUploadActionConfig() {
		
		OrphanUploadAction action = docOrphanUploadAction();
		ActionDefinition actionDef = new ActionDefinition(action.getActionName());
		
		SimpleNextStateBuilder<Object, ActionInput> nextStateBuilder = simpleDocNextStateBuilder();
		nextStateBuilder.addNextStateConfig(ObjectState.START_STATE, action.getActionName(), STATE_PENDING);
		
		ActionConfigurationImpl<DocInfo, DocPublishing, DocInfo, Boolean, DocActionResult<Boolean>> orphanActionConfig = 
				new ActionConfigurationImpl(actionDef, action, nextStateBuilder, permissionAuth);
		
		return orphanActionConfig;
	}
	
	@Bean
	public EditAction docEditAction() {
		return new EditAction();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Bean
	public ActionConfigurationImpl<DocInfo, DocPublishing, DocInfo, Boolean, DocActionResult<Boolean>> docEditActionConfig() {
		
		EditAction docEditAction = docEditAction();
		ActionDefinition actionDef = new ActionDefinition(docEditAction.getActionName());
		
		ActionConfigurationImpl<DocInfo, DocPublishing, DocInfo, Boolean, DocActionResult<Boolean>> editActionConfig = 
				new ActionConfigurationImpl(actionDef, docEditAction, noChangeStateBuilder, permissionAuth);
		
		return editActionConfig;
	}
	
	@Bean
	public PublishAction docPublishAction() {
		return new PublishAction();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Bean
	public ActionConfigurationImpl<DocInfo, DocPublishing, DocInfo, Boolean, DocActionResult<Boolean>> docPublishActionConfig() {
		
		PublishAction action = docPublishAction();
		ActionDefinition actionDef = new ActionDefinition(action.getActionName());
		
		SimpleNextStateBuilder<Object, ActionInput> nextStateBuilder = simpleDocNextStateBuilder();
		nextStateBuilder.addNextStateConfig(ObjectState.START_STATE, action.getActionName(), STATE_PUBLISHED);
		nextStateBuilder.addNextStateConfig(STATE_PENDING, action.getActionName(), STATE_PUBLISHED);
		
		ActionConfigurationImpl<DocInfo, DocPublishing, DocInfo, Boolean, DocActionResult<Boolean>> pubActionConfig = 
				new ActionConfigurationImpl(actionDef, action, nextStateBuilder, permissionAuth);
		
		return pubActionConfig;
	}
	
	@Bean
	public RejectAction docRejectAction() {
		return new RejectAction();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Bean
	public ActionConfigurationImpl<DocInfo, DocPublishing, DocInfo, Boolean, DocActionResult<Boolean>> 
			docRejectActionConfig() {
		
		RejectAction action = docRejectAction();
		ActionDefinition actionDef = new ActionDefinition(action.getActionName());
		
		SimpleNextStateBuilder<Object, ActionInput> nextStateBuilder = simpleDocNextStateBuilder();
		nextStateBuilder.addNextStateConfig(STATE_PENDING, action.getActionName(), STATE_REJECTED);
		
		ActionConfigurationImpl<DocInfo, DocPublishing, DocInfo, Boolean, DocActionResult<Boolean>> actionConfig = 
				new ActionConfigurationImpl(actionDef, action, nextStateBuilder, permissionAuth);
		
		return actionConfig;
	}
	
	@Bean
	public ActionRegistry<DocInfo, DocPublishing> docActionRegistry() {
		return new ActionRegistry<>();
	}
	
	@Bean
	public SimpleNextStateBuilder<Object, ActionInput> simpleDocNextStateBuilder() {
		return new SimpleNextStateBuilder<>();
	}
	
	
}
