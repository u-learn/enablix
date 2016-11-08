package com.enablix.content.approval.email;

import java.io.Serializable;

import com.enablix.content.approval.model.ContentApproval;
import com.enablix.state.change.model.ActionInput;

public class NotificationPayload implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private ActionInput actionInput;
	private ContentApproval contentRequest;
	
	public NotificationPayload(ActionInput actionInput, ContentApproval contentRequest) {
		this.actionInput = actionInput;
		this.contentRequest = contentRequest;
	}

	public ActionInput getActionInput() {
		return actionInput;
	}

	public void setActionInput(ActionInput actionInput) {
		this.actionInput = actionInput;
	}

	public ContentApproval getContentRequest() {
		return contentRequest;
	}

	public void setContentRequest(ContentApproval contentRequest) {
		this.contentRequest = contentRequest;
	}
	
}
