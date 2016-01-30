package com.enablix.core.domain.profile;

import com.enablix.core.domain.BaseWrappedEntity;

public class Profile extends BaseWrappedEntity {

	private String name;
	
	private ContactSet<Email> emails = new ContactSet<Email>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ContactSet<Email> getEmails() {
		return emails;
	}

	public void setEmails(ContactSet<Email> emails) {
		this.emails = emails;
	}
	
}
