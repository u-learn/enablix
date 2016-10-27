package com.enablix.content.approval.action;

import com.enablix.content.approval.ContentApprovalConstants;
import com.enablix.content.approval.model.ContentDetail;
import com.enablix.state.change.ActionException;
import com.enablix.state.change.model.GenericActionResult;

public class EditAction extends ContentSaveAction {

	@Override
	public String getActionName() {
		return ContentApprovalConstants.ACTION_EDIT;
	}

	@Override
	public GenericActionResult<ContentDetail, Boolean> execute(ContentDetail actionData, ContentDetail objectRef)
			throws ActionException {
		
		return copyInputData(actionData, objectRef);
	}

}
