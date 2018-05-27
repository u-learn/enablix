package com.enablix.core.activity.audit;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enablix.commons.util.StringUtil;
import com.enablix.core.domain.activity.ActivityChannel;
import com.enablix.core.domain.activity.ActivityChannel.Channel;

public class ActivityTrackingContext {

	private static final Logger LOGGER = LoggerFactory.getLogger(ActivityTrackingContext.class);
	
	public static final ThreadLocal<ActivityTrackingContext> THREAD_LOCAL_REQ_AUDIT_CONTEXT = new ThreadLocal<ActivityTrackingContext>() {
		protected ActivityTrackingContext initialValue() {
			return new ActivityTrackingContext();
		}
	};

	public static void initialize(Map<String, String> auditContextParams) {
		ActivityTrackingContext ctx = THREAD_LOCAL_REQ_AUDIT_CONTEXT.get();
		ctx.setAuditContextParams(auditContextParams);
	}
	
	public static ActivityTrackingContext get() {
		return THREAD_LOCAL_REQ_AUDIT_CONTEXT.get();
	}
	
	public static void clear() {
		
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("Clearing " + get());
		}
		
		THREAD_LOCAL_REQ_AUDIT_CONTEXT.remove();
	}
	
	
	private Map<String, String> auditContextParams;

	private ActivityTrackingContext(Map<String, String> auditContextParams) {
		super();
		this.auditContextParams = auditContextParams;
	}

	private ActivityTrackingContext() {
		this(new HashMap<String, String>());
	}

	public Map<String, String> getAuditContextParams() {
		return auditContextParams;
	}

	public void setAuditContextParams(Map<String, String> auditContextParams) {
		this.auditContextParams = auditContextParams;
	}
	
	public Channel getActivityChannel() {
		return getActivityChannel(null);
	}
	
	public ActivityChannel.Channel getActivityChannel(ActivityChannel.Channel defaultValue) {
		Channel channel = null;
		String actvyChannel = auditContextParams.get(ActivityTrackingConstants.ACTIVITY_CHANNEL);
		if (StringUtil.hasText(actvyChannel)) {
			channel = Channel.parse(actvyChannel);
		}
		return channel == null ? defaultValue : channel;
	}

	public String getActivityOrigin() {
		return auditContextParams.get(ActivityTrackingConstants.CONTEXT_ACTIVITY_ORIGIN);
	}
	
	public String getActivityContextId() {
		return auditContextParams.get(ActivityTrackingConstants.CONTEXT_ID);
	}
	
	public String getActivityContextName() {
		return auditContextParams.get(ActivityTrackingConstants.CONTEXT_NAME);
	}
	
	public String getActivityContextTerm() {
		return auditContextParams.get(ActivityTrackingConstants.CONTEXT_TERM);
	}
	
	public String getClientReferer() {
		return auditContextParams.get(ActivityTrackingConstants.REFERER);
	}
	
	public String getReferer() {
		String referer = auditContextParams.get(ActivityTrackingConstants.AT_REFERER);
		return referer == null ? getClientReferer() : referer;
	}
	
	public void setActivityContext(ActivityContextAware activity) {
		activity.setActivityOrigin(getActivityOrigin());
		activity.setContextId(getActivityContextId());
		activity.setContextName(getActivityContextName());
		activity.setContextTerm(getActivityContextTerm());
		activity.setReferer(getReferer());
	}
	
}
