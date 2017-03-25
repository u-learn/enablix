package com.enablix.core.domain.security.authorization;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.domain.BaseDocumentEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Document(collection = "ebx_user_profile")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserProfile extends BaseDocumentEntity {
	
	private String name;
	
	private String email;
	
	private UserSystemProfile systemProfile;
	
	private UserBusinessProfile businessProfile;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public UserSystemProfile getSystemProfile() {
		return systemProfile;
	}
	
	public void setSystemProfile(UserSystemProfile systemProfile) {
		this.systemProfile = systemProfile;
	}
	
	public UserBusinessProfile getBusinessProfile() {
		return businessProfile;
	}
	
	public void setBusinessProfile(UserBusinessProfile businessProfile) {
		this.businessProfile = businessProfile;
	}
	
}
