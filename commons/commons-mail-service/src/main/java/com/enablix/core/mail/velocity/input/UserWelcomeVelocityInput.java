package com.enablix.core.mail.velocity.input;

import com.enablix.core.domain.user.User;


public class UserWelcomeVelocityInput extends BaseVelocityInput implements LoggedInUserAware, RecipientUserAware, EnvPropertiesAware{

	private User loggedInUser;
	private User recipientUser;
	private String url;
	private String newUserId;
	
	public UserWelcomeVelocityInput(String newUserId) {
		this.newUserId = newUserId;
	}
	
	public User getLoggedInUser() {
		return loggedInUser;
	}
	
	public void setLoggedInUser(User loggedInUser) {
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

	public User getRecipientUser() {
		return recipientUser;
	}

	@Override
	public void setRecipientUser(User recipientUser) {
		this.recipientUser = recipientUser;
	}

	@Override
	public String getRecipientUserId() {
		return newUserId;
	}
	
}
