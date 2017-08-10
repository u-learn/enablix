package com.enablix.core.domain.user;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.commons.constants.AppConstants;
import com.enablix.core.domain.BaseDocumentEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Document(collection = AppConstants.SYSTEM_USER_COLL_NAME)
public class User extends BaseDocumentEntity {

	private String userId;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;
	
	private String tenantId;
	
	private Boolean isPasswordSet;
	
	private boolean system; // if this is an external system 
	
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

	public boolean isSystem() {
		return system;
	}

	public void setSystem(boolean system) {
		this.system = system;
	}

}