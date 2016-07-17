package com.enablix.commons.util.process;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessContext {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessContext.class);
	
	public static final ThreadLocal<ProcessContext> THREAD_LOCAL_PROCESS_CONTEXT = new ThreadLocal<ProcessContext>();

	public static void initialize(String userId, String tenantId, String templateId) {
		
		if (get() == null) {
		
			ProcessContext ctx = new ProcessContext(userId, tenantId, templateId);
			THREAD_LOCAL_PROCESS_CONTEXT.set(ctx);
			
			LOGGER.trace("Initialized " + ctx);

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
	}
	
	private String userId;
	private String tenantId;
	private String templateId;
	private String processId;

	private ProcessContext(String userId, String tenantId, String templateId) {
		super();
		this.userId = userId;
		this.tenantId = tenantId;
		this.templateId = templateId;
		this.processId = UUID.randomUUID().toString(); 
	}

	public String getUserId() {
		return userId;
	}

	public String getTenantId() {
		return tenantId;
	}

	public String getTemplateId() {
		return templateId;
	}

	@Override
	public String toString() {
		return "ProcessContext [userId=" + userId + ", tenantId=" + tenantId + ", templateId=" + templateId
				+ ", processId=" + processId + "]";
	}
	
}
