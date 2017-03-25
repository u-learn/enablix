package com.enablix.core.mail.velocity.input;

import com.enablix.core.domain.security.authorization.UserProfile;


public class UserWelcomeVelocityInput extends BaseVelocityInput implements LoggedInUserAware, RecipientUserAware, EnvPropertiesAware{

	private UserProfile loggedInUser;
	private UserProfile recipientUser;
	private String url;
	private String newUserId;
	private String password;
	
	public UserWelcomeVelocityInput(String newUserId) {
		this.newUserId = newUserId;
		this.password="";
	}
	
	
	public UserWelcomeVelocityInput(String newUserId, String password) {
		this.newUserId = newUserId;
		this.password=password;
	}
	
	public UserProfile getLoggedInUser() {
		return loggedInUser;
	}
	
	public void setLoggedInUser(UserProfile loggedInUser) {
		this.loggedInUser = loggedInUser;
	}
	
	public String getNewUserId() {
		return newUserId;
	}

	public void setNewUserId(String newUserId) {
		this.newUserId = newUserId;
	}

	@Override
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String getUrl() {
		return url;
	}

	public UserProfile getRecipientUser() {
		return recipientUser;
	}

	@Override
	public void setRecipientUser(UserProfile recipientUser) {
		this.recipientUser = recipientUser;
	}

	@Override
	public String getRecipientUserId() {
		return newUserId;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
