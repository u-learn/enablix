package com.enablix.doc.state.change.action;

import com.enablix.doc.state.change.DocStateChangeConstants;
import com.enablix.doc.state.change.model.DocActionResult;
import com.enablix.doc.state.change.model.DocInfo;
import com.enablix.state.change.StateChangeAction;
import com.enablix.state.change.model.SimpleActionInput;
import com.enablix.state.change.model.StateChangeRecording;

public class RejectAction implements StateChangeAction<DocInfo, SimpleActionInput, Boolean, DocActionResult<Boolean>> {

	@Override
	public String getActionName() {
		return DocStateChangeConstants.ACTION_REJECT;
	}

	@Override
	public DocActionResult<Boolean> execute(
			SimpleActionInput actionData, DocInfo objectRef, 
			StateChangeRecording<DocInfo> recording) {
		
		// Nothing to do. Just the state change is required which will be done by the
		// workflow manager.
		
		return new DocActionResult<Boolean>(objectRef, Boolean.TRUE);
	}

	@Override
	public Class<SimpleActionInput> getInputType() {
		return SimpleActionInput.class;
	}

}
