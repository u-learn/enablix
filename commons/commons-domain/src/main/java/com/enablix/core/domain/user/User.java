package com.enablix.core.domain.user;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.domain.BaseDocumentEntity;
import com.enablix.core.domain.profile.Profile;
import com.enablix.core.domain.security.authorization.UserRole;

@Document(collection = "ebxUser")
public class User extends BaseDocumentEntity {

	private String userId;
	
	private String name;

	private String password;
	
	private String tenantId;
	
	private Profile profile;
	
	private Boolean isPasswordSet;
	
	@Transient
	private UserRole userRole;	
	

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	public Boolean getIsPasswordSet() {
		return isPasswordSet;
	}

	public void setIsPasswordSet(Boolean isPasswordSet) {
		this.isPasswordSet = isPasswordSet;
	}

	public String getUserId() {
		return userId;
	}
	
	public String getName() {
		return name;
	};

	public void setName(String name) {
		this.name = name;
	};

	public void setUserId(String userId) {
		this.userId = userId;
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
