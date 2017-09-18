package com.enablix.content.quality.rule;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enablix.commons.util.beans.SpringBackedBeanRegistry;
import com.enablix.content.quality.QualityRule;

@Component
public class QualityRuleFactoryImpl extends SpringBackedBeanRegistry<QualityRule> implements QualityRuleFactory {

	private Map<String, QualityRule> rules = new HashMap<>();
	
	@Override
	protected Class<QualityRule> lookupForType() {
		return QualityRule.class;
	}

	@Override
	protected void registerBean(QualityRule bean) {
		rules.put(bean.getRuleId(), bean);
	}

	@Override
	public QualityRule getRule(String ruleId) {
		return rules.get(ruleId);
	}

	@Override
	public Collection<QualityRule> getAllRules() {
		return rules.values();
	}

}
