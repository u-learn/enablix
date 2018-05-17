package com.enablix.content.quality;

import com.enablix.core.domain.content.quality.AlertLevel;

public class LevelBasedRuleSet implements QualityRuleSet {
	
	public static QualityRuleSet ERROR_RULES = new LevelBasedRuleSet(new AlertLevel[] {AlertLevel.ERROR}); // error level rules 
	public static QualityRuleSet WARN_RULES = new LevelBasedRuleSet(new AlertLevel[] {AlertLevel.WARN}); // warning alert level rules

	private AlertLevel[] ruleLevels;
	
	public LevelBasedRuleSet(AlertLevel[] levels) {
		this.ruleLevels = levels;
	}

	public boolean contains(QualityRule rule) {
		
		if (rule != null) {
			
			for (AlertLevel level : ruleLevels) {
				if (level == rule.getAlertLevel()) {
					return true;
				}
			}
		}
		
		return false;
	}
	
}