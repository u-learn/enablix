package com.enablix.core.api;

public class SignupRequest {

	private String email;
	
	private String name;
	
	private String password;
	
	private String orgName;

	public String getEmail() {
		return email;
	}

	public void setEmail(String adminEmailId) {
		this.email = adminEmailId;
	}

	public String getName() {
		return name;
	}

	public void setName(String adminName) {
		this.name = adminName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String adminPassword) {
		this.password = adminPassword;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
}
