package com.enablix.content.quality;

import java.util.HashSet;
import java.util.Set;

public class RuleIdBasedRuleSet implements QualityRuleSet {

	private Set<String> ruleIds;
	
	public RuleIdBasedRuleSet(Set<String> ruleIds) {
		this.ruleIds = ruleIds;
	}
	
	public RuleIdBasedRuleSet(String[] ruleIds) {
		this.ruleIds = new HashSet<>();
		for (String ruleId : ruleIds) {
			this.ruleIds.add(ruleId);
		}
	}

	@Override
	public boolean contains(QualityRule rule) {
		return ruleIds.contains(rule.getRuleId());
	}
	
}
