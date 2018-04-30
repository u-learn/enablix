package com.enablix.app.mail.generic;

import com.enablix.core.mail.BasicEmailVelocityInput;

public class LoginReminderMailInput extends BasicEmailVelocityInput {

	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
