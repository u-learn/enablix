package com.enablix.content.quality;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.content.quality.ContentAuthorResolver.Author;
import com.enablix.content.quality.repo.ContentQualityAlertRepository;
import com.enablix.content.quality.rule.QualityRuleFactory;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.QualityRuleType;
import com.enablix.core.domain.content.quality.ContentQualityAlert;
import com.enablix.core.domain.content.quality.QualityAlert;
import com.enablix.core.domain.content.quality.QualityAnalysis;
import com.enablix.data.segment.view.DataSegmentInfoBuilder;
import com.enablix.services.util.ContentDataUtil;

@Component
public class QualityAnalyzerImpl implements QualityAnalyzer {

	private static final Logger LOGGER = LoggerFactory.getLogger(QualityAnalyzerImpl.class);
	
	@Autowired
	private ContentQualityAlertRepository alertRepo;
	
	@Autowired
	private QualityRuleFactory ruleFactory;
	
	@Autowired
	private DataSegmentInfoBuilder dsInfoBuilder;
	
	@Autowired
	private ContentAuthorResolver authorResolver;
	
	@Override
	public QualityAnalysis analyze(Map<String, Object> content, String contentQId, 
			QualityRuleSet ruleSet, TemplateFacade template) {
		
		QualityAnalysis qa = new QualityAnalysis();

		if (ruleSet != null) {
		
			List<String> qualityRuleIds = template.getApplicableQualityRules(contentQId);
			
			if (CollectionUtil.isNotEmpty(qualityRuleIds)) {
				
				for (String ruleId : qualityRuleIds) {
					
					QualityRule rule = ruleFactory.getRule(ruleId);
					
					if (rule == null) {
						
						LOGGER.error("Invalid quality rule [{}]", ruleId);
						
					} else if (ruleSet.contains(rule)) {
						
						QualityRuleType ruleType = template.getQualityRule(ruleId);
						
						Collection<QualityAlert> alerts = rule.evaluate(
								ruleType.getConfigParams(), content, contentQId, template);
						
						if (CollectionUtil.isNotEmpty(alerts)) {
							qa.addAlerts(alerts);
						}
						
						qa.addRuleExecuted(ruleId);
					}
					
				}
			}
		}
		
		return qa;
	}

	@Override
	public QualityAnalysis analyzeAndRecord(Map<String, Object> content, String contentQId,
			QualityRuleSet ruleSet, TemplateFacade template) {
		
		QualityAnalysis analysis = analyze(content, contentQId, ruleSet, template);
		String contentIdentity = ContentDataUtil.getRecordIdentity(content);
		
		if (CollectionUtil.isNotEmpty(analysis.getRulesExecuted())) {
			
			alertRepo.deleteByRecordIdentityAndAlertRuleIdIn(contentIdentity, analysis.getRulesExecuted());
			
			if (analysis.hasAlerts()) {

				Author author = authorResolver.getAuthor(content, contentQId);
				
				List<ContentQualityAlert> qAlerts = new ArrayList<>();
				
				for (QualityAlert alert : analysis.getAlerts()) {
					
					ContentQualityAlert cqAlert = new ContentQualityAlert(
							contentQId, contentIdentity, author.getUserId(), author.getName(), alert);
					
					cqAlert.setDataSegmentInfo(dsInfoBuilder.build(cqAlert));
					
					qAlerts.add(cqAlert);
				}
				
				alertRepo.save(qAlerts);
			}
		}
		
		return analysis;
	}
	
	
}

	
	
