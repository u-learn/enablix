package com.enablix.app.audit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.audit.repo.ActivityAuditRepository;
import com.enablix.commons.util.concurrent.Events;
import com.enablix.core.domain.activity.ActivityAudit;
import com.enablix.core.mq.EventSubscription;

@Component
public class ActivityAuditor {

	@Autowired
	private ActivityAuditRepository auditRepo;
	
	@EventSubscription(eventName = {Events.AUDIT_ACITIVITY})
	public void auditActivity(ActivityAudit activity) {
		auditRepo.save(activity);
	}
	
}
