package com.enablix.core.mongo.audit.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.Query;

import com.enablix.core.domain.activity.ActivityAudit;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface ActivityAuditRepository extends BaseMongoRepository<ActivityAudit> {

	@Query("{'activity.contextName' : ?0, 'activity.activityType' : ?1, 'createdAt' : {$gte : ?2, $lt: ?3} }")
	List<ActivityAudit> findByActivityContextNameAndActivityTypeAndCreatedAtBetween(
			String contextName, String activityType, Date startDate, Date endDate);

}

