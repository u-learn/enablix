package com.enablix.content.approval.web;

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

import com.enablix.content.approval.ContentApprovalConstants;
import com.enablix.content.approval.model.ContentApproval;
import com.enablix.content.approval.model.ContentDetail;
import com.enablix.content.approval.repo.ContentApprovalRepository;
import com.enablix.state.change.ActionException;
import com.enablix.state.change.StateChangeWorkflowManager;
import com.enablix.state.change.definition.ActionDefinition;
import com.enablix.state.change.impl.ActionRegistry;
import com.enablix.state.change.model.SimpleActionInput;

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
	public void submitContent(@RequestBody ContentDetail contentDetails) throws ActionException {
		
		LOGGER.debug("Content workfow submit request");
		
		wfManager.start(ContentApprovalConstants.WORKFLOW_NAME, 
				ContentApprovalConstants.ACTION_SUBMIT, contentDetails);
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/approve/", consumes = "application/json")
	public void approveContent(@RequestBody SimpleActionInput actionInput) throws ActionException {
		
		LOGGER.debug("Content workfow approve request");
		
		wfManager.executeAction(ContentApprovalConstants.WORKFLOW_NAME, actionInput.getIdentity(), 
				ContentApprovalConstants.ACTION_APPROVE, actionInput);
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/reject/", consumes = "application/json")
	public void rejectContent(@RequestBody SimpleActionInput actionInput) throws ActionException {
		
		LOGGER.debug("Content workfow reject request");
		
		wfManager.executeAction(ContentApprovalConstants.WORKFLOW_NAME, actionInput.getIdentity(), 
				ContentApprovalConstants.ACTION_REJECT, actionInput);
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/withdraw/", consumes = "application/json")
	public void withdrawContent(@RequestBody SimpleActionInput actionInput) throws ActionException {
		
		LOGGER.debug("Content workfow withdraw request");
		
		wfManager.executeAction(ContentApprovalConstants.WORKFLOW_NAME, actionInput.getIdentity(), 
				ContentApprovalConstants.ACTION_WITHDRAW, actionInput);
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/edit/", consumes = "application/json")
	public void editContent(@RequestBody ContentDetail contentDetails) throws ActionException {
		
		LOGGER.debug("Content workfow reject request");
		
		wfManager.executeAction(ContentApprovalConstants.WORKFLOW_NAME, contentDetails.getIdentity(), 
				ContentApprovalConstants.ACTION_EDIT, contentDetails);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/r/{refObjectIdentity}/", produces = "application/json")
	public ContentApproval getContentRequest(@PathVariable String refObjectIdentity) {
		LOGGER.debug("Fetch content approval record: {}", refObjectIdentity);
		return repo.findByObjectRefIdentity(refObjectIdentity);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/actionmap/", produces = "application/json")
	public Map<String, List<ActionDefinition>> getStateActionMapping() {
		LOGGER.debug("Fetching state to action mapping");
		return contentActionRegistry.getStateActionMap();
	}
	
}