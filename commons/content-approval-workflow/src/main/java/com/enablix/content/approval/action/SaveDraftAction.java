package com.enablix.content.approval.action;

import com.enablix.content.approval.ContentApprovalConstants;

public class SaveDraftAction extends SubmitAction {

	@Override
	public String getActionName() {
		return ContentApprovalConstants.ACTION_SAVE_DRAFT;
	}

}
