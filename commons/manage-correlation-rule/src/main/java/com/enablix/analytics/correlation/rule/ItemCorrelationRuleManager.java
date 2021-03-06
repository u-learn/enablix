package com.enablix.analytics.correlation.rule;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBException;

import com.enablix.core.commons.xsdtopojo.ContentCorrelatedItemType;
import com.enablix.core.commons.xsdtopojo.ItemCorrelationRuleType;
import com.enablix.core.commons.xsdtopojo.ItemUserCorrelationRuleType;

public interface ItemCorrelationRuleManager {

	void saveItemItemCorrelationRule(ItemCorrelationRuleType rule);
	
	void saveItemUserCorrelationRule(ItemUserCorrelationRuleType rule);

	void saveItemItemCorrelationRuleXml(InputStream templateXmlInputStream) throws JAXBException;

	void saveItemUserCorrelationRuleXml(InputStream xmlInputStream) throws JAXBException;
	
	List<ItemCorrelationRuleType> getItemItemCorrelationRulesForTriggerItemQId(String triggerItemQId);
	
	List<ItemUserCorrelationRuleType> getItemUserCorrelationRulesForTriggerItemQId(String triggerItemQId);
	
	Set<String> getCorrelatedItemQIdsForTriggerItemQId(String triggerItemQId);

	List<ContentCorrelatedItemType> getContentCorrelatedItemTypeHierarchy(String sourceItemQId, int depth);
	
}
