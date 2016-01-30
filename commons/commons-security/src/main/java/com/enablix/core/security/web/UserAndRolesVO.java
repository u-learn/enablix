package com.enablix.core.security.web;

import java.util.ArrayList;
import java.util.List;

import com.enablix.core.domain.user.User;

public class UserAndRolesVO {

	private User user;
	
	private List<String> roles = new ArrayList<>();

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
	
}
