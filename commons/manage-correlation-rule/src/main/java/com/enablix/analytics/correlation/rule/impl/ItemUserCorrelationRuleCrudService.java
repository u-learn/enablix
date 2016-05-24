package com.enablix.analytics.correlation.rule.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.analytics.correlation.repo.ItemUserCorrelationRuleRepository;
import com.enablix.app.service.MongoRepoCrudService;
import com.enablix.core.correlation.ItemUserCorrelationRuleDocument;
import com.enablix.core.mongo.repository.BaseMongoRepository;

@Component
public class ItemUserCorrelationRuleCrudService extends MongoRepoCrudService<ItemUserCorrelationRuleDocument> {

	@Autowired
	private ItemUserCorrelationRuleRepository repo;
	
	@Override
	public BaseMongoRepository<ItemUserCorrelationRuleDocument> getRepository() {
		return repo;
	}

	@Override
	public ItemUserCorrelationRuleDocument findExisting(ItemUserCorrelationRuleDocument t) {
		return repo.findByItemUserCorrelationRuleId(t.getItemUserCorrelationRule().getId());
	}
	
	@Override
	protected ItemUserCorrelationRuleDocument merge(ItemUserCorrelationRuleDocument t,
			ItemUserCorrelationRuleDocument existing) {
		existing.setItemUserCorrelationRule(t.getItemUserCorrelationRule());
		return existing;
	}

	
	
}
