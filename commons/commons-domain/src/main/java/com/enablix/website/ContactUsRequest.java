package com.enablix.website;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.domain.BaseDocumentEntity;

@Document(collection="ebx_contact_us")
public class ContactUsRequest extends BaseDocumentEntity {

	private String name;
	private String companyName;
	private String email;
	private String message;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ContactUsLead [name=" + name + ", companyName=" + companyName + ", email=" + email + ", message="
				+ message + "]";
	}
	
}
