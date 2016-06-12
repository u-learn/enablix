package com.enablix.core.domain.activity;

public class NonRegisteredActor extends Actor {

	private String externalId;
	
	private String name;
	
	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public ActorType getType() {
		return ActorType.NON_REGISTERED;
	}

}
