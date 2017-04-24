package com.enablix.app.main;

import java.io.IOException;

import org.springframework.core.io.Resource;

public class ClientBasedResourcePathBuilder extends AbstractResourcePathBuilder {

	@Override
	public Resource createCustomLocation(Resource location) throws IOException {
		
		final String tenantId = resolveTenantId();
		final String clientId = resolveClientId();
		
		return tenantId != null && clientId != null ? 
				location.createRelative("/custom/" + tenantId + "/" + clientId + "/") : null;
	}
	
}
