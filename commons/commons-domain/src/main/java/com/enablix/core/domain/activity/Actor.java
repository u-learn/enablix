package com.enablix.core.domain.activity;

public abstract class Actor {
	
	public enum ActorType {
		REGISTERED, NON_REGISTERED
	}
	
	protected String userId;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public abstract ActorType getType();
	
}
