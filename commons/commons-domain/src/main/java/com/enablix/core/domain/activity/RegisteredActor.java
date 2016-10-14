package com.enablix.core.domain.activity;

public class RegisteredActor extends Actor {

	private String userId;
	
	private String name;
	
	public RegisteredActor(String userId, String name) {
		super();
		this.userId = userId;
		this.name = name;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public ActorType getType() {
		return ActorType.REGISTERED;
	}

}
