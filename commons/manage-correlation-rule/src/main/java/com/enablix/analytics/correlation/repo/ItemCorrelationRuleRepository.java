package com.enablix.analytics.correlation.repo;

import java.util.List;

import com.enablix.core.correlation.ItemCorrelationRuleDocument;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface ItemCorrelationRuleRepository extends BaseMongoRepository<ItemCorrelationRuleDocument> {

	ItemCorrelationRuleDocument findByItemCorrelationRuleId(String ruleId);
	
	List<ItemCorrelationRuleDocument> findByItemCorrelationRuleTriggerItemQualifiedId(String triggerItemQId);
	
}
