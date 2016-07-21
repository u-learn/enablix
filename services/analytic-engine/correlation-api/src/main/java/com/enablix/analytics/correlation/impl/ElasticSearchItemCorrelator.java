package com.enablix.analytics.correlation.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.analytics.correlation.ItemItemCorrelator;
import com.enablix.analytics.correlation.matcher.DataMatcher;
import com.enablix.analytics.correlation.matcher.MatchInputRecord;
import com.enablix.analytics.correlation.rule.ItemCorrelationRuleManager;
import com.enablix.app.content.ContentDataUtil;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.commons.xsdtopojo.ItemCorrelationRuleType;
import com.enablix.core.commons.xsdtopojo.RelatedItemType;
import com.enablix.core.commons.xsdtopojo.RelatedItemsType;

@Component
public class ElasticSearchItemCorrelator implements ItemItemCorrelator {

	private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchItemCorrelator.class);
	
	@Autowired
	private ItemCorrelationRuleManager itemCorrRuleMgr;
	
	@Autowired
	private MatchInputRecordBuilder matchInputBuilder;
	
	@Autowired
	private DataMatcher dataMatcher;
	
	@Autowired
	private ItemItemCorrelationRecorder itemCorrRecorder;
	
	@Override
	public void correlateItem(ContentTemplate template, ContentDataRef item) {
		
		List<ItemCorrelationRuleType> itemCorrRules = 
				itemCorrRuleMgr.getItemItemCorrelationRulesForTriggerItemQId(item.getContainerQId());
		
		if (!itemCorrRules.isEmpty()) {
		
			for (ItemCorrelationRuleType rule : itemCorrRules) {
				LOGGER.debug("Processing correlation rule [{}] on trigger item [{}]", rule.getName(), item);
				processItemCorrelationRule(rule, template, item);
			}
		}
		
	}
	
	/*
	 * Process:
	 * 
	 * 1. Filter the trigger item record based on the trigger item filter in the rule
	 * 2. If no record found after filter, them skip rule processing
	 * 3. Navigate through related item path recording items if defined as related item
	 */
	private void processItemCorrelationRule(ItemCorrelationRuleType corrRule, 
			ContentTemplate template, ContentDataRef item) {

		MatchInputRecord matchInput = matchInputBuilder.buildTriggerMatchInput(template, corrRule.getTriggerItem(), item);
		
		if (matchInput != null) {
			for (RelatedItemType relatedItemDef : corrRule.getRelatedItems().getRelatedItem()) {
				processRelatedItemRule(corrRule, template, item, matchInput, relatedItemDef);
			}
		}
		
	}

	private void processRelatedItemRule(ItemCorrelationRuleType corrRule, ContentTemplate template, ContentDataRef triggerItem,
			MatchInputRecord matchInput, RelatedItemType relatedItemDef) {
		
		LOGGER.debug("Finding related item: {}", relatedItemDef.getQualifiedId());
		
		List<Map<String, Object>> matchedRecords = dataMatcher.findMatchingRecords(template, relatedItemDef, matchInput);
		
		for (Map<String, Object> match : matchedRecords) {
			
			if (relatedItemDef.isRecordAsRelated()) {
				
				ContentDataRef relatedItem = ContentDataUtil.contentDataToRef(
						match, template.getId(), relatedItemDef.getQualifiedId());
				
				itemCorrRecorder.recordItemCorrelation(triggerItem, relatedItem, corrRule, relatedItemDef.getTags());
			}
			
			RelatedItemsType relatedItemsDef = relatedItemDef.getRelatedItems();
			
			if (relatedItemsDef != null) {
			
				for (RelatedItemType nextRelatedItemDef : relatedItemsDef.getRelatedItem()) {
					
					if (nextRelatedItemDef != null) {
						
						MatchInputRecord nextMatchInput = new MatchInputRecord(
								triggerItem.getContainerQId(), relatedItemDef.getQualifiedId(), match);
						nextMatchInput.setParent(matchInput);
						
						processRelatedItemRule(corrRule, template, triggerItem, nextMatchInput, nextRelatedItemDef);
					}
				}
			}
		}
	}

	@Override
	public void deleteCorrelationForItem(ContentDataRef item) {
		itemCorrRecorder.removeItemCorrelations(item);
	}

}
