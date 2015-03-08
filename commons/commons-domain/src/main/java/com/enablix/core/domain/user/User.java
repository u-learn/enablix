package com.enablix.core.domain.user;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.domain.BaseEntity;
import com.enablix.core.domain.profile.Profile;

@Document
public class User extends BaseEntity {

	private String userId;
	
	private Profile profile;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	
}
