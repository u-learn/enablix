package com.enablix.core.domain.user;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.domain.BaseDocumentEntity;
import com.enablix.core.domain.profile.Profile;

@Document(collection = "ebxUser")
public class User extends BaseDocumentEntity {

	private String userId;
	
	private String password;
	
	private String tenantId;
	
	private Profile profile = new Profile();
	
	private Boolean isPasswordSet;
	
	public Boolean getIsPasswordSet() {
		return isPasswordSet;
	}

	public void setIsPasswordSet(Boolean isPasswordSet) {
		this.isPasswordSet = isPasswordSet;
	}

	public String getUserId() {
		return userId.toLowerCase();
	}

	public void setUserId(String userId) {
		this.userId = userId.toLowerCase();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTenantId() { 
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	
}
