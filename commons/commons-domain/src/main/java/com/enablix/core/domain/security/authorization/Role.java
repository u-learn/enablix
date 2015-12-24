package com.enablix.core.domain.security.authorization;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.domain.BaseDocumentEntity;

@Document(collection="ebx_role")
public class Role extends BaseDocumentEntity {

	private String roleName;
	
	private List<String> permissions;

	public String getRoleName() {
		return roleName;
	}

	public List<String> getPermissions() {
		return permissions;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}
	
}
