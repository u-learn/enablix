package com.enablix.core.security.web;

import java.util.ArrayList;
import java.util.List;

import com.enablix.core.domain.security.authorization.Role;
import com.enablix.core.domain.user.User;

public class UserAndRolesVO {

	private User user;
	
	private List<String> roles = new ArrayList<>();
	
	private List<Role> detailedRoles = new ArrayList<>();

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	
	public void addRole(String roleIdentity) {
		roles.add(roleIdentity);
	}

	public List<Role> getDetailedRoles() {
		return detailedRoles;
	}

	public void setDetailedRoles(List<Role> detailedRoles) {
		this.detailedRoles = new ArrayList<>();
		for (Role role : detailedRoles) {
			addDetailedRole(role);
		}
	}
	
	public void addDetailedRole(Role role) {
		detailedRoles.add(role);
		if (role.getIdentity() != null) {
			roles.add(role.getIdentity());
		}
	}
	
}
