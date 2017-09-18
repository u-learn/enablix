package com.enablix.content.quality;

import java.util.Map;

import com.enablix.core.api.TemplateFacade;
import com.enablix.core.domain.content.quality.QualityAnalysis;

public interface QualityAnalyzer {

	public enum AnalysisRuleSet {
		ALL, NONE,
		ERROR, // error level rules 
		WARN // warning alert level rules
	}
	
	QualityAnalysis analyze(Map<String, Object> content, String contentQId, 
			AnalysisRuleSet analysisLevel, TemplateFacade template);
	
	QualityAnalysis analyzeAndRecord(Map<String, Object> content, String contentQId, 
			AnalysisRuleSet analysisLevel, TemplateFacade template);
	
}
