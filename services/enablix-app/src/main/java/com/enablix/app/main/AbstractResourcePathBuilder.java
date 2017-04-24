package com.enablix.app.main;

import com.enablix.commons.util.process.ProcessContext;
import com.enablix.commons.util.process.RequestContext;

public abstract class AbstractResourcePathBuilder implements CustomResourcePathBuilder {

	protected String resolveTenantId() {
		
		String tenantId = null;
		
		ProcessContext processContext = ProcessContext.get();
		
		if (processContext != null) {
			
			tenantId = processContext.getTenantId();
			
		} else {
			RequestContext requestContext = RequestContext.get();
			if (requestContext != null) {
				tenantId = requestContext.getTenantId();
			}
		}
		
		return tenantId;
	}

	protected String resolveClientId() {
		
		String clientId = null;
		
		ProcessContext processContext = ProcessContext.get();
		
		if (processContext != null) {
			
			clientId = processContext.getClientId();
			
		} else {
			RequestContext requestContext = RequestContext.get();
			if (requestContext != null) {
				clientId = requestContext.getClientId();
			}
		}
		
		return clientId;
	}
	
}
