package com.enablix.doc.state.change.model;

import com.enablix.state.change.model.ActionInput;

public class PublishInfo implements ActionInput {
	
	private String docIdentity;
	
	private String docContainerIdentity;

	private String parentIdentity;
	
	private String docQId;

	public String getDocIdentity() {
		return docIdentity;
	}

	public void setDocIdentity(String docIdentity) {
		this.docIdentity = docIdentity;
	}

	public String getDocContainerIdentity() {
		return docContainerIdentity;
	}

	public void setDocContainerIdentity(String docContainerIdentity) {
		this.docContainerIdentity = docContainerIdentity;
	}

	public String getParentIdentity() {
		return parentIdentity;
	}

	public void setParentIdentity(String parentIdentity) {
		this.parentIdentity = parentIdentity;
	}

	public String getDocQId() {
		return docQId;
	}

	public void setDocQId(String docQId) {
		this.docQId = docQId;
	}
	
}
