package com.enablix.core.domain.profile;

public class Email extends Contact {

	private String emailId;

	public Email() {
	}
	
	public Email(String emailId) {
		this.emailId = emailId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	
}
