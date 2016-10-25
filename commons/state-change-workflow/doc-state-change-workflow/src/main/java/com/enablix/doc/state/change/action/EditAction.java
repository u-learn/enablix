package com.enablix.doc.state.change.action;

import com.enablix.doc.state.change.DocStateChangeConstants;
import com.enablix.doc.state.change.model.DocActionResult;
import com.enablix.doc.state.change.model.DocInfo;
import com.enablix.state.change.StateChangeAction;

public class EditAction implements StateChangeAction<DocInfo, DocInfo, Boolean, DocActionResult<Boolean>> {

	@Override
	public String getActionName() {
		return DocStateChangeConstants.ACTION_EDIT;
	}

	@Override
	public DocActionResult<Boolean> execute(DocInfo actionData, DocInfo objectRef) {
		
		objectRef.setTitle(actionData.getTitle());
		objectRef.setNotes(actionData.getNotes());
		objectRef.setMetadata(actionData.getMetadata());
		
		return new DocActionResult<Boolean>(objectRef, Boolean.TRUE);
	}

	@Override
	public Class<DocInfo> getInputType() {
		return DocInfo.class;
	}

}
