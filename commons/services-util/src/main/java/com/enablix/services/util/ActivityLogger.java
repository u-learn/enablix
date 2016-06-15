package com.enablix.services.util;

import com.enablix.commons.util.concurrent.Events;
import com.enablix.core.domain.activity.ActivityAudit;
import com.enablix.core.mq.Event;
import com.enablix.core.mq.util.EventUtil;

public class ActivityLogger {

	public static void auditActivity(ActivityAudit activity) {
		EventUtil.publishEvent(new Event<ActivityAudit>(Events.AUDIT_ACITIVITY, activity));
	}
	
}
