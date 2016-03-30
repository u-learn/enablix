package com.enablix.core.mail.velocity.input;

import com.enablix.core.domain.tenant.Tenant;

public abstract class BaseVelocityInput implements TenantAware {

	private Tenant tenant;

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}
	
}
