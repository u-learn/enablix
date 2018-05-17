package com.enablix.content.quality.repo;

import java.util.Collection;
import java.util.List;

import com.enablix.core.domain.content.quality.ContentQualityAlert;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface ContentQualityAlertRepository extends BaseMongoRepository<ContentQualityAlert> {

	Long deleteByRecordIdentityAndAlertRuleId(String recordIdentity, String ruleId);
	
	Long deleteByRecordIdentityAndAlertRuleIdIn(String recordIdentity, Collection<String> ruleIds);

	List<ContentQualityAlert> findByRecordIdentity(String recordIdentity);

}
