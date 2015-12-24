package com.enablix.core.domain.security.authorization;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.domain.BaseDocumentEntity;

@Document(collection = "ebx_user_role")
public class UserRole extends BaseDocumentEntity {

	private String userIdentity;
	
	@DBRef
	private List<Role> roles;

	public String getUserIdentity() {
		return userIdentity;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setUserIdentity(String userIdentity) {
		this.userIdentity = userIdentity;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
}
