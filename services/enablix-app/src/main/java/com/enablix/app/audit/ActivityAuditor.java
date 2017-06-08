package com.enablix.app.audit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.concurrent.Events;
import com.enablix.commons.util.date.DateUtil;
import com.enablix.core.domain.activity.ActivityAudit;
import com.enablix.core.mongo.audit.repo.ActivityAuditRepository;
import com.enablix.core.mq.EventSubscription;

@Component
public class ActivityAuditor {

	@Autowired
	private ActivityAuditRepository auditRepo;
	
	@EventSubscription(eventName = {Events.AUDIT_ACITIVITY})
	public void auditActivity(ActivityAudit activity) {
		
		if (activity.getDateDimension() == null) {
			activity.setDateDimension(
					DateUtil.getDateDimension(activity.getActivityTime()));
		}
		
		auditRepo.save(activity);
	}
	
}
