package com.enablix.content.quality;

public interface QualityRuleSet {

	boolean contains(QualityRule rule);
		
	QualityRuleSet ALL = (rl) -> { return true; };	
	QualityRuleSet NONE = (rl) -> { return false; };
		
}
