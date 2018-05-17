package com.enablix.content.quality;

import java.util.Map;

import com.enablix.core.api.TemplateFacade;
import com.enablix.core.domain.content.quality.QualityAnalysis;

public interface QualityAnalyzer {
	
	QualityAnalysis analyze(Map<String, Object> content, String contentQId, 
			QualityRuleSet ruleSet, TemplateFacade template);
	
	QualityAnalysis analyzeAndRecord(Map<String, Object> content, String contentQId, 
			QualityRuleSet ruleSet, TemplateFacade template);
	
}
