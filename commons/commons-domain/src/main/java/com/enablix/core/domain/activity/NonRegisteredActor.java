package com.enablix.core.domain.activity;

public class NonRegisteredActor extends Actor {

	private String externalId;
	
	private String name;
	
	@SuppressWarnings("unused")
	private NonRegisteredActor() {
		// for ORM
	}
	
	public NonRegisteredActor(String externalId) {
		this(externalId, externalId);
	}
	
	public NonRegisteredActor(String externalId, String name) {
		super();
		this.externalId = externalId;
		this.name = name;
		setUserId(externalId);
	}

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
