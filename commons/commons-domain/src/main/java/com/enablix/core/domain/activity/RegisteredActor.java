package com.enablix.core.domain.activity;

public class RegisteredActor extends Actor {

	private String name;
	
	public RegisteredActor(String userId, String name) {
		super();
		this.userId = userId;
		this.name = name;
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
