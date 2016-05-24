package com.enablix.analytics.correlation.repo;

import java.util.List;

import com.enablix.core.correlation.ItemUserCorrelationRuleDocument;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface ItemUserCorrelationRuleRepository extends BaseMongoRepository<ItemUserCorrelationRuleDocument> {

	ItemUserCorrelationRuleDocument findByItemUserCorrelationRuleId(String ruleId);
	
	List<ItemUserCorrelationRuleDocument> findByItemUserCorrelationRuleTriggerItemQualifiedId(String triggerItemQId);
	
}
