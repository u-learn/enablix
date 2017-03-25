package com.enablix.core.domain.user;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.commons.constants.AppConstants;
import com.enablix.core.domain.BaseDocumentEntity;

@Document(collection = AppConstants.SYSTEM_USER_COLL_NAME)
public class User extends BaseDocumentEntity {

	private String userId;
	
	private String password;
	
	private String tenantId;
	
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

}