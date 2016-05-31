package com.enablix.trigger.lifecycle.rule.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.service.MongoRepoCrudService;
import com.enablix.core.domain.trigger.TriggerLifecycleRule;
import com.enablix.core.mongo.repository.BaseMongoRepository;
import com.enablix.trigger.lifecycle.rule.repo.TriggerLifecycleRuleRepository;

@Component
public class TriggerLifecycleRuleCrudService extends MongoRepoCrudService<TriggerLifecycleRule> {

	@Autowired
	private TriggerLifecycleRuleRepository repo;
	
	@Override
	public BaseMongoRepository<TriggerLifecycleRule> getRepository() {
		return repo;
	}

	@Override
	public TriggerLifecycleRule findExisting(TriggerLifecycleRule t) {
		return repo.findByContentTriggerRuleId(t.getContentTriggerRule().getId());
	}
	
	@Override
	protected TriggerLifecycleRule merge(TriggerLifecycleRule t, TriggerLifecycleRule existing) {
		existing.setContentTriggerRule(t.getContentTriggerRule());
		return existing;
	}

}
