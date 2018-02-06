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

	@Query(value = "{'activity.activityType' : 'CONTENT_ACCESS', 'activity.itemIdentity' : ?0, 'actor.userId': ?1, 'activityTime' : {$gte : ?2, $lt: ?3} }", count = true)
	Long countContentAccessByUserBetweenDates(String recIdentity, String userId, Date startDate, Date endDate);

	@Query(value = "{'activity.activityType' : 'CONTENT_ACCESS', 'activity.containerQId' : ?0, 'actor.userId': { $in: ?1}, 'activityTime' : {$gte : ?2, $lt: ?3} }", count = true)
	Long countContentTypeAccessByUsersBetweenDates(String containerQId, List<String> userIds, Date startDate, Date endDate);

	@Query(value = "{'activity.activityType' : 'CONTENT_ACCESS', 'activity.itemIdentity' : ?0, 'actor.userId': { $in: ?1}, 'activityTime' : {$gte : ?2, $lt: ?3} }", count = true)
	Long countContentAccessByUsersBetweenDates(String contentIdentity, List<String> userIds, Date startDate, Date endDate);

}

