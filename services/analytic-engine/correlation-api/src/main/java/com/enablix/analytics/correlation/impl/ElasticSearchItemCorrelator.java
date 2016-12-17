package com.enablix.analytics.correlation.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.analytics.correlation.ItemItemCorrelator;
import com.enablix.analytics.correlation.context.CorrelationContext;
import com.enablix.analytics.correlation.matcher.DataMatcher;
import com.enablix.analytics.correlation.matcher.MatchInputRecord;
import com.enablix.analytics.correlation.rule.ItemCorrelationRuleManager;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.commons.xsdtopojo.ItemCorrelationRuleType;
import com.enablix.core.commons.xsdtopojo.RelatedItemType;
import com.enablix.core.commons.xsdtopojo.RelatedItemsType;
import com.enablix.services.util.ContentDataUtil;

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
	public void correlateItem(ContentDataRef item, CorrelationContext context) {
		
		List<ItemCorrelationRuleType> itemCorrRules = 
				itemCorrRuleMgr.getItemItemCorrelationRulesForTriggerItemQId(item.getContainerQId());
		
		if (!itemCorrRules.isEmpty()) {
		
			for (ItemCorrelationRuleType rule : itemCorrRules) {
				LOGGER.debug("Processing correlation rule [{}] on trigger item [{}]", rule.getName(), item);
				processItemCorrelationRule(rule, context, item);
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
			CorrelationContext context, ContentDataRef item) {

		itemCorrRecorder.removeItemCorrelationsByRule(item, corrRule);
		
		MatchInputRecord matchInput = matchInputBuilder.buildTriggerMatchInput(
				context.getTemplate(), corrRule.getTriggerItem(), item);
		
		if (matchInput != null) {
			for (RelatedItemType relatedItemDef : corrRule.getRelatedItems().getRelatedItem()) {
				processRelatedItemRule(corrRule, context, item, matchInput, relatedItemDef);
			}
		}
		
	}

	private void processRelatedItemRule(ItemCorrelationRuleType corrRule, CorrelationContext context, 
			ContentDataRef triggerItem, MatchInputRecord matchInput, RelatedItemType relatedItemDef) {
		
		LOGGER.debug("Finding related item: {}", relatedItemDef.getQualifiedId());
		
		boolean relateAndRecordContainerType = relatedItemDef.isRecordAsRelated() 
				&& context.shouldCorrelateContainer(relatedItemDef.getQualifiedId());

		// only relate if the container record being related is in list of containers in correlation
		// scope and is marked to be recorded as related; OR if there are any child items to be related
		// i.e. there is further path to be navigated
		if (relateAndRecordContainerType || relatedItemDef.getRelatedItems() != null) {
		
			List<Map<String, Object>> matchedRecords = dataMatcher.findMatchingRecords(
					context.getTemplate(), relatedItemDef, matchInput);
			
			for (Map<String, Object> match : matchedRecords) {

				// record the matched content records
				if (relateAndRecordContainerType) {
					
					ContentDataRef relatedItem = ContentDataUtil.contentDataToRef(
							match, context.getTemplate(), relatedItemDef.getQualifiedId());
					
					itemCorrRecorder.recordItemCorrelation(triggerItem, relatedItem, corrRule, relatedItemDef.getTags());
				}
				
				// continue correlating the next level in navigation path of related item
				RelatedItemsType relatedItemsDef = relatedItemDef.getRelatedItems();
				
				if (relatedItemsDef != null) {
				
					for (RelatedItemType nextRelatedItemDef : relatedItemsDef.getRelatedItem()) {
						
						if (nextRelatedItemDef != null) {
							
							MatchInputRecord nextMatchInput = new MatchInputRecord(
									triggerItem.getContainerQId(), relatedItemDef.getQualifiedId(), match);
							nextMatchInput.setParent(matchInput);
							
							processRelatedItemRule(corrRule, context, triggerItem, nextMatchInput, nextRelatedItemDef);
						}
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
