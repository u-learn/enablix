package com.enablix.content.approval.action;

import org.springframework.beans.BeanUtils;

import com.enablix.content.approval.ContentApprovalConstants;
import com.enablix.content.approval.model.ContentDetail;
import com.enablix.state.change.ActionException;
import com.enablix.state.change.model.GenericActionResult;

public class EditAction extends BaseContentAction<ContentDetail, Boolean> {

	public EditAction() {
		super(ContentDetail.class);
	}
	
	@Override
	public String getActionName() {
		return ContentApprovalConstants.ACTION_EDIT;
	}

	@Override
	public GenericActionResult<ContentDetail, Boolean> execute(ContentDetail actionData, ContentDetail objectRef)
			throws ActionException {
		
		BeanUtils.copyProperties(actionData, objectRef);
		
		return new GenericActionResult<ContentDetail, Boolean>(objectRef, Boolean.TRUE);
	}

}
