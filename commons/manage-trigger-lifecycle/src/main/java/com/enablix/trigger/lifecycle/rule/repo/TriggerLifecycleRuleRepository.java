package com.enablix.trigger.lifecycle.rule.repo;

import com.enablix.core.commons.xsdtopojo.TriggerTypeEnum;
import com.enablix.core.domain.trigger.TriggerLifecycleRule;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface TriggerLifecycleRuleRepository extends BaseMongoRepository<TriggerLifecycleRule> {

	TriggerLifecycleRule findByContentTriggerRuleId(String ruleId);
	
	TriggerLifecycleRule findByContentTriggerRuleTypeAndContentTriggerRuleCandidateContainersContainerQId(TriggerTypeEnum triggerType, String containerQId);
	
}
