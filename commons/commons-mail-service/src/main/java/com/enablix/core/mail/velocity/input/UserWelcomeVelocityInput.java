package com.enablix.core.mail.velocity.input;

import com.enablix.core.domain.user.User;

public class UserWelcomeVelocityInput extends BaseVelocityInput {

	private User loggedInUser;
	private User newCreatedUser;
	
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
	
	public User getNewCreatedUser() {
		return newCreatedUser;
	}
	
	public void setNewCreatedUser(User newCreatedUser) {
		this.newCreatedUser = newCreatedUser;
	}

	public String getNewUserId() {
		return newUserId;
	}

	public void setNewUserId(String newUserId) {
		this.newUserId = newUserId;
	}
	
}
