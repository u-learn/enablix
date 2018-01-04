package com.enablix.app.mail.web;

import java.util.List;

public class EmailData {

	private String containerQId;

	private String contentIdentity;

	@Deprecated
	// Deprecated in favor of emailIds
	private String emailId;
	private String emailCustomContent;
	
	private List<String> emailIds;

	public String getEmailCustomContent() {
		return emailCustomContent;
	}

	public void setEmailCustomContent(String emailCustomContent) {
		this.emailCustomContent = emailCustomContent;
	}
	public String getEmailId() {
		return emailId;
	}

	public void setEmailid(String emailId) {
		this.emailId = emailId;
	}

	public String getContainerQId() {
		return containerQId;
	}

	public void setContainerQId(String containerQId) {
		this.containerQId = containerQId;
	}

	public String getContentIdentity() {
		return contentIdentity;
	}

	public void setContentIdentity(String instanceIdentity) {
		this.contentIdentity = instanceIdentity;
	}

	public List<String> getEmailIds() {
		return emailIds;
	}

	public void setEmailIds(List<String> emailIds) {
		this.emailIds = emailIds;
	}

}
