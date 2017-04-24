package com.enablix.commons.util.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestContext {

	private static final Logger LOGGER = LoggerFactory.getLogger(RequestContext.class);
	
	public static final ThreadLocal<RequestContext> THREAD_LOCAL_REQUEST_CONTEXT = new ThreadLocal<RequestContext>();

	public static void initialize(String tenantId, String clientId) {
		
		if (get() == null) {
		
			RequestContext ctx = new RequestContext(tenantId, clientId);
			THREAD_LOCAL_REQUEST_CONTEXT.set(ctx);
		
			if (LOGGER.isTraceEnabled())
				LOGGER.trace("Initialized: " + ctx);
			
		} else {
			throw new IllegalStateException("Request context cannot be re-initialized");
		}
	}
	
	public static RequestContext get() {
		return THREAD_LOCAL_REQUEST_CONTEXT.get();
	}
	
	public static void clear() {
		
		if (LOGGER.isTraceEnabled()) 
			LOGGER.trace("Clearing " + get());
		
		THREAD_LOCAL_REQUEST_CONTEXT.remove();
	}
	
	private String tenantId;
	private String clientId;

	private RequestContext(String tenantId, String clientId) {
		super();
		this.tenantId = tenantId;
		this.clientId = clientId;
	}

	public String getTenantId() {
		return tenantId;
	}

	public String getClientId() {
		return clientId;
	}

	@Override
	public String toString() {
		return "RequestContext [tenantId=" + tenantId + ", clientId=" + clientId + "]";
	}
	
}
