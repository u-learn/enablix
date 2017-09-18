package com.enablix.content.quality.rule;

import java.util.Collection;

import com.enablix.content.quality.QualityRule;

public interface QualityRuleFactory {

	QualityRule getRule(String ruleId);
	
	Collection<QualityRule> getAllRules();
	
}
