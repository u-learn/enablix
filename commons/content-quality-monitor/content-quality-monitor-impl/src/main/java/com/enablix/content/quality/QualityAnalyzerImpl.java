package com.enablix.content.quality;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.content.quality.rule.QualityRuleFactory;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.QualityRuleType;
import com.enablix.core.domain.content.quality.ContentQualityAnalysis;
import com.enablix.core.domain.content.quality.QualityAlert;
import com.enablix.core.domain.content.quality.QualityAnalysis;
import com.enablix.services.util.ContentDataUtil;

@Component
public class QualityAnalyzerImpl implements QualityAnalyzer {

	private static final Logger LOGGER = LoggerFactory.getLogger(QualityAnalyzerImpl.class);
	
	@Autowired
	private ContentQualityAlertsCrud crud;
	
	@Autowired
	private QualityRuleFactory ruleFactory;
	
	@Override
	public QualityAnalysis analyze(Map<String, Object> content, String contentQId, 
			AnalysisRuleSet analysisLevel, TemplateFacade template) {
		
		QualityAnalysis qa = new QualityAnalysis();

		if (analysisLevel != AnalysisRuleSet.NONE) {
		
			List<String> qualityRuleIds = template.getApplicableQualityRules(contentQId);
			
			if (CollectionUtil.isNotEmpty(qualityRuleIds)) {
				
				for (String ruleId : qualityRuleIds) {
					
					QualityRule rule = ruleFactory.getRule(ruleId);
					
					if (rule == null) {
						
						LOGGER.error("Invalid quality rule [{}]", ruleId);
						
					} else {
			
						if (analysisLevel == AnalysisRuleSet.ALL
								|| analysisLevelMatchesRuleAlertLevel(analysisLevel, rule)) {
						
							QualityRuleType ruleType = template.getQualityRule(ruleId);
							
							Collection<QualityAlert> alerts = rule.evaluate(
									ruleType.getConfigParams(), content, contentQId, template);
							
							qa.addAlerts(alerts);
							
						}
					}
					
				}
			}
		}
		
		return qa;
	}

	private boolean analysisLevelMatchesRuleAlertLevel(AnalysisRuleSet analysisLevel, QualityRule rule) {
		return analysisLevel.name().equals(rule.getAlertLevel().name());
	}

	@Override
	public QualityAnalysis analyzeAndRecord(Map<String, Object> content, String contentQId,
			AnalysisRuleSet analysisLevel, TemplateFacade template) {
		
		QualityAnalysis analysis = analyze(content, contentQId, analysisLevel, template);
		String contentIdentity = ContentDataUtil.getRecordIdentity(content);
		
		if (analysis.hasAlerts()) {
			
			ContentQualityAnalysis contentQA = 
					new ContentQualityAnalysis(contentIdentity, contentQId);
			contentQA.addAlerts(analysis.getAlerts());
			crud.saveOrUpdate(contentQA);
			
		} else {
			crud.deleteByContentIdentity(contentIdentity);
		}

		return analysis;
	}
}

	
	
