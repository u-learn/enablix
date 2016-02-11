package com.enablix.core.domain;

import com.enablix.core.api.IdentityAware;

public abstract class BaseEntity implements IdentityAware {

	private String identity;

	@Override
	public String getIdentity() {
		return identity;
	}

	@Override
	public void setIdentity(String identity) {
		this.identity = identity;
	}
	
}
