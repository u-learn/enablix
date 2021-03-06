package com.enablix.analytics.correlation.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.analytics.correlation.ItemUserCorrelator;
import com.enablix.analytics.correlation.context.CorrelationContext;
import com.enablix.analytics.correlation.matcher.DataMatcher;
import com.enablix.analytics.correlation.matcher.MatchInputRecord;
import com.enablix.analytics.correlation.matcher.UserMatcher;
import com.enablix.analytics.correlation.rule.ItemCorrelationRuleManager;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ItemUserCorrelationRuleType;
import com.enablix.core.commons.xsdtopojo.PathItemType;
import com.enablix.core.commons.xsdtopojo.RelatedUserPathType;
import com.enablix.core.commons.xsdtopojo.RelatedUserType;
import com.enablix.core.domain.security.authorization.UserProfile;
import com.enablix.services.util.TemplateUtil;

@Component
public class ItemUserCorrelatorImpl implements ItemUserCorrelator {

	private static final Logger LOGGER = LoggerFactory.getLogger(ItemUserCorrelatorImpl.class);
	
	@Autowired
	private ItemCorrelationRuleManager corrRuleMgr;
	
	@Autowired
	private DataMatcher dataMatcher;
	
	@Autowired
	private UserMatcher userMatcher;
	
	@Autowired
	private ItemUserCorrelationRecorder itemUserCorrRecorder;
	
	@Autowired
	private MatchInputRecordBuilder matchInputBuilder;
	
	@Override
	public void correlateUsers(ContentDataRef item, CorrelationContext context) {

		List<ItemUserCorrelationRuleType> corrRules = 
				corrRuleMgr.getItemUserCorrelationRulesForTriggerItemQId(item.getContainerQId());
		
		if (!corrRules.isEmpty()) {
			
			for (ItemUserCorrelationRuleType rule : corrRules) {
				LOGGER.debug("Processing item user correlation rule [{}] on trigger item [{}]", rule.getName(), item);
				processItemUserCorrelationRule(rule, context, item);
			}
		}
		
	}

	private void processItemUserCorrelationRule(ItemUserCorrelationRuleType corrRule, CorrelationContext context,
			ContentDataRef triggerItem) {
		
		itemUserCorrRecorder.removeItemUserCorrelationByRule(triggerItem, corrRule);
		
		TemplateFacade template = context.getTemplate();
		
		MatchInputRecord matchInput = matchInputBuilder.buildTriggerMatchInput(template, corrRule.getTriggerItem(), triggerItem);

		if (matchInput != null) {
			for (RelatedUserPathType relatedItemDef : corrRule.getRelatedUsers().getRelatedUserPath()) {
				processRelatedUserRule(corrRule, template, triggerItem, matchInput, relatedItemDef);
			}
		}
		
	}

	private void processRelatedUserRule(ItemUserCorrelationRuleType corrRule, TemplateFacade template,
			ContentDataRef item, MatchInputRecord matchInput, RelatedUserPathType relatedUserPath) {
		
		PathItemType pathItem = relatedUserPath.getPathItem();
		if (pathItem != null) {
			processRelatedPathItemRule(template, item, matchInput, pathItem, corrRule);
		}
		
		RelatedUserType relatedUser = relatedUserPath.getRelatedUser();
		if (relatedUser != null) {
			processRelatedUserPath(template, item, corrRule, relatedUser, matchInput);
		}
		
	}

	private void processRelatedPathItemRule(TemplateFacade template, ContentDataRef triggerItem, 
			MatchInputRecord matchInput, PathItemType pathItem, ItemUserCorrelationRuleType rule) {
		
		List<Map<String, Object>> matchedRecords = 
				dataMatcher.findMatchingRecords(template, pathItem, matchInput);
		
		RelatedUserType relatedUserDef = pathItem.getRelatedUser();
		
		if (relatedUserDef != null) {
			
			for (Map<String, Object> match : matchedRecords) {
				
				MatchInputRecord nextMatchInput = new MatchInputRecord(
						triggerItem.getContainerQId(), pathItem.getQualifiedId(), match);
				nextMatchInput.setParent(matchInput);
				
				processRelatedUserPath(template, triggerItem, rule, relatedUserDef, nextMatchInput);
			}
			
		}
		
		PathItemType nextPathItem = pathItem.getPathItem();
		
		if (nextPathItem != null) {
			
			for (Map<String, Object> match : matchedRecords) {
				
				MatchInputRecord nextMatchInput = new MatchInputRecord(
						triggerItem.getContainerQId(), pathItem.getQualifiedId(), match);
				nextMatchInput.setParent(matchInput);
				
				processRelatedPathItemRule(template, triggerItem, nextMatchInput, nextPathItem, rule);
			}
				
		}
	}

	private void processRelatedUserPath(TemplateFacade template, ContentDataRef item, ItemUserCorrelationRuleType rule,
			RelatedUserType relatedUserDef, MatchInputRecord nextMatchInput) {
		
		String userContainerQId = TemplateUtil.getUserContainerQId(template.getTemplate());
		List<UserProfile> users = userMatcher.findMatchingUsers(template, 
				userContainerQId, relatedUserDef, nextMatchInput);
		
		if (!users.isEmpty()) {
			for (UserProfile user : users) {
				itemUserCorrRecorder.recordItemUserCorrelation(item, user, rule, relatedUserDef.getTags());
			}
		}
	}

	@Override
	public void deleteCorrelationForItem(ContentDataRef item) {
		itemUserCorrRecorder.removeItemUserCorrelation(item);
	}
	
}
