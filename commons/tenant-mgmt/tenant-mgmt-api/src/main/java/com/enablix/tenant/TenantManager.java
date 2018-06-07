package com.enablix.tenant;

import com.enablix.core.api.SignupRequest;

public interface TenantManager {

	void setupTenant(SignupRequest request) throws Exception;
	
	boolean doesTenantExist(String tenantId);
	
}
