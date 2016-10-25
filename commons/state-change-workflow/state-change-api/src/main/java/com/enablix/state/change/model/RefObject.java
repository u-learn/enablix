package com.enablix.state.change.model;

import com.enablix.core.api.IdentityAware;

public abstract class RefObject implements IdentityAware {

	private String identity;
	
	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}
	
}
