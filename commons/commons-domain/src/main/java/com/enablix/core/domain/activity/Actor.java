package com.enablix.core.domain.activity;

public abstract class Actor {
	
	public enum ActorType {
		REGISTERED, NON_REGISTERED
	}
	
	public abstract ActorType getType();
	
}
