package com.enablix.commons.util.process;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class ProcessContext {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessContext.class);
	
	private static final String LOG_TENANT_ID_KEY = "TENANT_ID";
	private static final String LOG_PROCESS_ID_KEY = "PROCESS_ID";
	
	public static final ThreadLocal<ProcessContext> THREAD_LOCAL_PROCESS_CONTEXT = new ThreadLocal<ProcessContext>();

	public static void initialize(String userId, String userDisplayName, String tenantId, String templateId, String clientId) {
		
		if (get() == null) {
		
			ProcessContext ctx = new ProcessContext(userId, userDisplayName, tenantId, templateId, clientId);
			THREAD_LOCAL_PROCESS_CONTEXT.set(ctx);
			
			LOGGER.trace("Initialized " + ctx);
			
			postInitialization(ctx);

		} else {
			throw new IllegalStateException("Process context cannot be re-initialized");
		}
	}
	
	public static ProcessContext get() {
		return THREAD_LOCAL_PROCESS_CONTEXT.get();
	}
	
	public static void clear() {
		LOGGER.trace("Clearing " + get());
		THREAD_LOCAL_PROCESS_CONTEXT.remove();
		postDestroy();
	}
	
	private static void postInitialization(final ProcessContext context) {
		
		if (null != context) {
		
			if (null != context.processId) {
				MDC.put(LOG_PROCESS_ID_KEY, context.processId);
			}
			
			if (null != context.getTenantId()) {
				MDC.put(LOG_TENANT_ID_KEY, context.getTenantId());
			}
		}
	}
	
	private static void postDestroy() {
		MDC.put(LOG_PROCESS_ID_KEY, "");
		MDC.put(LOG_TENANT_ID_KEY, "");
	}
	
	private String userId;
	private String userDisplayName;
	private String tenantId;
	private String templateId;
	private String processId;
	private String clientId;

	private ProcessContext(String userId, String userDisplayName, String tenantId, String templateId, String clientId) {
		super();
		this.userId = userId;
		this.userDisplayName = userDisplayName;
		this.tenantId = tenantId;
		this.templateId = templateId;
		this.clientId = clientId;
		this.processId = UUID.randomUUID().toString();
	}

	public String getUserId() {
		return userId;
	}

	public String getUserDisplayName() {
		return userDisplayName;
	}

	public String getTenantId() {
		return tenantId;
	}

	public String getTemplateId() {
		return templateId;
	}

	public String getClientId() {
		return clientId;
	}

	@Override
	public String toString() {
		return "ProcessContext [userId=" + userId + ", tenantId=" + tenantId + ", templateId=" + templateId
				+ ", processId=" + processId + ", clientId=" + clientId + "]";
	}
	
}
