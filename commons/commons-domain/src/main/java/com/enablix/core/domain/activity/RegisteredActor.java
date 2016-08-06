package com.enablix.core.domain.activity;

public class RegisteredActor extends Actor {

	private String userId;
	
	public RegisteredActor(String userId) {
		super();
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public ActorType getType() {
		return ActorType.REGISTERED;
	}

}
