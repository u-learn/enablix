package com.enablix.app.main;

import java.io.IOException;

import org.springframework.core.io.Resource;

public class TenantBasedResourcePathBuilder extends AbstractResourcePathBuilder {

	@Override
	public Resource createCustomLocation(Resource location) throws IOException {
		final String tenantId = resolveTenantId();
		return tenantId != null ? location.createRelative("/custom/" + tenantId + "/") : null;
	}
	
}
