package com.enablix.analytics.correlation.rule.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.analytics.correlation.repo.ItemCorrelationRuleRepository;
import com.enablix.app.service.MongoRepoCrudService;
import com.enablix.core.correlation.ItemCorrelationRuleDocument;
import com.enablix.core.mongo.repository.BaseMongoRepository;

@Component
public class ItemCorrelationRuleCrudService extends MongoRepoCrudService<ItemCorrelationRuleDocument> {

	@Autowired
	private ItemCorrelationRuleRepository repo;
	
	@Override
	public BaseMongoRepository<ItemCorrelationRuleDocument> getRepository() {
		return repo;
	}

	@Override
	public ItemCorrelationRuleDocument findExisting(ItemCorrelationRuleDocument t) {
		return repo.findByItemCorrelationRuleId(t.getItemCorrelationRule().getId());
	}
	
	@Override
	protected ItemCorrelationRuleDocument merge(ItemCorrelationRuleDocument t, ItemCorrelationRuleDocument existing) {
		existing.setItemCorrelationRule(t.getItemCorrelationRule());
		return existing;
	}

}
