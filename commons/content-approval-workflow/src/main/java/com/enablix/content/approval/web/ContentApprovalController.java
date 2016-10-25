package com.enablix.content.approval.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.content.approval.ContentApprovalConstants;
import com.enablix.content.approval.model.ContentDetail;
import com.enablix.state.change.ActionException;
import com.enablix.state.change.StateChangeWorkflowManager;
import com.enablix.state.change.model.SimpleActionInput;

@RestController
@RequestMapping("contentwf")
public class ContentApprovalController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContentApprovalController.class);
	
	@Autowired
	private StateChangeWorkflowManager wfManager;
	
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
	
	@RequestMapping(method = RequestMethod.POST, value="/edit/", consumes = "application/json")
	public void editContent(@RequestBody ContentDetail contentDetails) throws ActionException {
		
		LOGGER.debug("Content workfow reject request");
		
		wfManager.executeAction(ContentApprovalConstants.WORKFLOW_NAME, contentDetails.getIdentity(), 
				ContentApprovalConstants.ACTION_REJECT, contentDetails);
	}
	
}