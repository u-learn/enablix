package com.enablix.content.approval.action;

import com.enablix.content.approval.ContentApprovalConstants;
import com.enablix.content.approval.model.ContentDetail;
import com.enablix.state.change.ActionException;
import com.enablix.state.change.model.GenericActionResult;
import com.enablix.state.change.model.SimpleActionInput;
import com.enablix.state.change.model.StateChangeRecording;

public class WithdrawAction extends BaseContentAction<SimpleActionInput, Boolean> {

	public WithdrawAction() {
		super(SimpleActionInput.class);
	}
	
	@Override
	public String getActionName() {
		return ContentApprovalConstants.ACTION_WITHDRAW;
	}

	@Override
	public GenericActionResult<ContentDetail, Boolean> execute(
				SimpleActionInput actionData, ContentDetail objectRef, 
				StateChangeRecording<ContentDetail> recording)
			throws ActionException {
		// Nothing to do. Only state change is required which will be done by the framework
		return new GenericActionResult<ContentDetail, Boolean>(objectRef, Boolean.TRUE);
	}

}
