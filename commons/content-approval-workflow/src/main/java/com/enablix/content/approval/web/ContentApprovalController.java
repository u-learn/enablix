package com.enablix.content.approval.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.commons.util.PermissionConstants;
import com.enablix.content.approval.ContentApprovalConstants;
import com.enablix.content.approval.ContentApprovalUtil;
import com.enablix.content.approval.model.ContentApproval;
import com.enablix.content.approval.model.ContentDetail;
import com.enablix.content.approval.repo.ContentApprovalRepository;
import com.enablix.core.activity.audit.ActivityTrackingContext;
import com.enablix.core.domain.activity.Activity.ActivityType;
import com.enablix.core.domain.activity.ActivityChannel.Channel;
import com.enablix.core.security.SecurityUtil;
import com.enablix.services.util.ActivityLogger;
import com.enablix.state.change.ActionException;
import com.enablix.state.change.StateChangeConstants;
import com.enablix.state.change.StateChangeWorkflowManager;
import com.enablix.state.change.definition.ActionDefinition;
import com.enablix.state.change.impl.ActionRegistry;
import com.enablix.state.change.model.ActionInput;
import com.enablix.state.change.model.SimpleActionInput;
import com.enablix.state.change.model.StateChangeRecording;

@RestController
@RequestMapping("contentwf")
public class ContentApprovalController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContentApprovalController.class);
	
	@Autowired
	private StateChangeWorkflowManager wfManager;

	@Autowired
	private ContentApprovalRepository repo;
	
	@Autowired
	@Qualifier("contentActionRegistry")
	private ActionRegistry<ContentDetail, ContentApproval> contentActionRegistry;
	
	@RequestMapping(method = RequestMethod.POST, value="/submit/", consumes = "application/json")
	public StateChangeRecording<?> submitContent(@RequestBody ContentDetail contentDetails) throws ActionException {
		
		LOGGER.debug("Content workfow submit request");
		
		return wfManager.start(ContentApprovalConstants.WORKFLOW_NAME, 
				ContentApprovalConstants.ACTION_SUBMIT, contentDetails);
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/savedraft/", consumes = "application/json")
	public StateChangeRecording<?> saveDraft(@RequestBody ContentDetail contentDetails) throws ActionException {
		
		LOGGER.debug("Content workfow save draft request");
		
		return wfManager.start(ContentApprovalConstants.WORKFLOW_NAME, 
				ContentApprovalConstants.ACTION_SAVE_DRAFT, contentDetails);
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/approve/", consumes = "application/json")
	public StateChangeRecording<?> approveContent(@RequestBody SimpleActionInput actionInput) throws ActionException {
		
		LOGGER.debug("Content workfow approve request");
		
		return wfManager.executeAction(ContentApprovalConstants.WORKFLOW_NAME, actionInput.getIdentity(), 
				ContentApprovalConstants.ACTION_APPROVE, actionInput);
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/approve/list/", consumes = "application/json")
	public List<StateChangeRecording<?>> approveContents(@RequestBody List<SimpleActionInput> actionInput) throws ActionException {
		LOGGER.debug("Content workfow approve request list");
		return executeActionOnList(actionInput, (in) -> approveContent(in));
	}
	
	private <I extends ActionInput> List<StateChangeRecording<?>> executeActionOnList(
			List<I> actionInput, WFAction<I> func) throws ActionException {
		
		List<StateChangeRecording<?>> response = new ArrayList<>();
		
		for (I in : actionInput) {
			response.add(func.execute(in));
		}
		
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/publish/", consumes = "application/json")
	public StateChangeRecording<?> publishContent(@RequestBody ContentDetail contentDetails) throws ActionException {
		
		LOGGER.debug("Content workfow publish request");
		
		if (SecurityUtil.currentUserHasAllPermission(PermissionConstants.PERMISSION_VIEW_STUDIO)) {
			
			return wfManager.executeAction(ContentApprovalConstants.WORKFLOW_NAME, contentDetails.getIdentity(), 
				ContentApprovalConstants.ACTION_PUBLISH, contentDetails);
			
		} else {
			return wfManager.executeAction(ContentApprovalConstants.WORKFLOW_NAME, contentDetails.getIdentity(), 
					ContentApprovalConstants.ACTION_SUBMIT, contentDetails);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/publish/list/", consumes = "application/json")
	public List<StateChangeRecording<?>> publishContents(@RequestBody List<ContentDetail> contentDetails) throws ActionException {
		LOGGER.debug("Content workfow publish request list");
		return executeActionOnList(contentDetails, (in) -> publishContent(in));
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/reject/", consumes = "application/json")
	public StateChangeRecording<?> rejectContent(@RequestBody SimpleActionInput actionInput) throws ActionException {
		
		LOGGER.debug("Content workfow reject request");
		
		return wfManager.executeAction(ContentApprovalConstants.WORKFLOW_NAME, actionInput.getIdentity(), 
				ContentApprovalConstants.ACTION_REJECT, actionInput);
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/reject/list/", consumes = "application/json")
	public List<StateChangeRecording<?>> rejectContents(@RequestBody List<SimpleActionInput> actionInput) throws ActionException {
		LOGGER.debug("Content workfow reject request list");
		return executeActionOnList(actionInput, (in) -> rejectContent(in));
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/discard/", consumes = "application/json")
	public StateChangeRecording<?> discardDraftContent(@RequestBody SimpleActionInput actionInput) throws ActionException {
		
		LOGGER.debug("Content workfow discard request");
		
		return wfManager.executeAction(ContentApprovalConstants.WORKFLOW_NAME, actionInput.getIdentity(), 
				StateChangeConstants.ACTION_DISCARD, actionInput);
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/discard/list/", consumes = "application/json")
	public List<StateChangeRecording<?>> discardDraftContents(@RequestBody List<SimpleActionInput> actionInput) throws ActionException {
		LOGGER.debug("Content workfow discard request list");
		return executeActionOnList(actionInput, (in) -> discardDraftContent(in));
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/withdraw/", consumes = "application/json")
	public StateChangeRecording<?> withdrawContent(@RequestBody SimpleActionInput actionInput) throws ActionException {
		
		LOGGER.debug("Content workfow withdraw request");
		
		return wfManager.executeAction(ContentApprovalConstants.WORKFLOW_NAME, actionInput.getIdentity(), 
				ContentApprovalConstants.ACTION_WITHDRAW, actionInput);
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/withdraw/list/", consumes = "application/json")
	public List<StateChangeRecording<?>> withdrawContents(@RequestBody List<SimpleActionInput> actionInput) throws ActionException {
		LOGGER.debug("Content workfow withdraw request list");
		return executeActionOnList(actionInput, (in) -> withdrawContent(in));
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/edit/", consumes = "application/json")
	public StateChangeRecording<?> editContent(@RequestBody ContentDetail contentDetails) throws ActionException {
		
		LOGGER.debug("Content workfow reject request");
		
		return wfManager.executeAction(ContentApprovalConstants.WORKFLOW_NAME, contentDetails.getIdentity(), 
				ContentApprovalConstants.ACTION_EDIT, contentDetails);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/r/{refObjectIdentity}/", produces = "application/json")
	public ContentApproval getContentRequest(@PathVariable String refObjectIdentity) {
		
		LOGGER.debug("Fetch content approval record: {}", refObjectIdentity);
		ContentApproval content = repo.findByObjectRefIdentity(refObjectIdentity);
		
		Channel channel = ActivityTrackingContext.get().getActivityChannel();
		
		if (channel != null) {
		
			if (content.getCurrentState().getStateName().equals(
					ContentApprovalConstants.STATE_PENDING_APPROVAL)) {
				
				ActivityLogger.auditContentActivity(
						ContentApprovalUtil.createAuditActivityInstance(
								content, ActivityType.CONTENT_SUGGEST_VIEW), 
						channel);
			}
		}
		
		return content;
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/actionmap/", produces = "application/json")
	public Map<String, List<ActionDefinition>> getStateActionMapping() {
		LOGGER.debug("Fetching state to action mapping");
		return contentActionRegistry.getStateActionMap();
	}
	
	private interface WFAction<I extends ActionInput> {
		StateChangeRecording<?> execute(I in) throws ActionException;
	}
	
}