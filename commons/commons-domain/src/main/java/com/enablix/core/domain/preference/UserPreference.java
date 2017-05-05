package com.enablix.core.domain.preference;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.domain.config.Settings;

@Document( collection = "ebx_user_preference")
public class UserPreference extends Settings {

	private String userId;
	
	public UserPreference() {
		super();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
