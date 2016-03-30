package com.enablix.core.mail.velocity.input;

import com.enablix.core.domain.tenant.Tenant;

public interface TenantAware {

	void setTenant(Tenant tenant);
	
	Tenant getTenant();
	
}
