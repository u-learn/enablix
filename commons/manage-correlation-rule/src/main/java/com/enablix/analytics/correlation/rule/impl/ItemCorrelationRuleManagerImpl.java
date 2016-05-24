package com.enablix.analytics.correlation.rule.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.analytics.correlation.repo.ItemCorrelationRuleRepository;
import com.enablix.analytics.correlation.repo.ItemUserCorrelationRuleRepository;
import com.enablix.analytics.correlation.rule.ItemCorrelationRuleManager;
import com.enablix.core.commons.xsd.parser.XMLParser;
import com.enablix.core.commons.xsd.parser.XMLParserRegistry;
import com.enablix.core.commons.xsdtopojo.ItemCorrelationRuleType;
import com.enablix.core.commons.xsdtopojo.ItemCorrelationRules;
import com.enablix.core.commons.xsdtopojo.ItemUserCorrelationRuleType;
import com.enablix.core.commons.xsdtopojo.ItemUserCorrelationRules;
import com.enablix.core.correlation.ItemCorrelationRuleDocument;
import com.enablix.core.correlation.ItemUserCorrelationRuleDocument;

@Component
public class ItemCorrelationRuleManagerImpl implements ItemCorrelationRuleManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(ItemCorrelationRuleManagerImpl.class);
	
	@Autowired
	private ItemCorrelationRuleCrudService itemCorrRuleCrud;
	
	@Autowired
	private ItemUserCorrelationRuleCrudService itemUserCorrRuleCrud;
	
	@Autowired
	private XMLParserRegistry xmlParserRegistry;

	@Autowired
	private ItemCorrelationRuleRepository itemCorrRuleRepo;
	
	@Autowired
	private ItemUserCorrelationRuleRepository itemUserCorrRuleRepo;
	
	@Override
	public void saveItemItemCorrelationRule(ItemCorrelationRuleType rule) {
		ItemCorrelationRuleDocument ruleDoc = new ItemCorrelationRuleDocument();
		ruleDoc.setIdentity(rule.getId());
		ruleDoc.setItemCorrelationRule(rule);
		itemCorrRuleCrud.saveOrUpdate(ruleDoc);
	}

	@Override
	public void saveItemUserCorrelationRule(ItemUserCorrelationRuleType rule) {
		ItemUserCorrelationRuleDocument ruleDoc = new ItemUserCorrelationRuleDocument();
		ruleDoc.setIdentity(rule.getId());
		ruleDoc.setItemUserCorrelationRule(rule);
		itemUserCorrRuleCrud.saveOrUpdate(ruleDoc);
	}
	
	@Override
	public void saveItemItemCorrelationRuleXml(InputStream xmlInputStream) throws JAXBException {
		
		XMLParser<ItemCorrelationRules> parser = xmlParserRegistry.getXMLParser(ItemCorrelationRules.class);
		if (parser == null) {
			LOGGER.error("No xml parser found for ItemCorrelationRules");
			throw new IllegalStateException("No xml parser found for ItemCorrelationRules");
		}
		
		ItemCorrelationRules rules = parser.unmarshal(xmlInputStream);

		for (ItemCorrelationRuleType rule : rules.getItemCorrelationRule()) {
			saveItemItemCorrelationRule(rule);
		}
		
	}
	
	@Override
	public void saveItemUserCorrelationRuleXml(InputStream xmlInputStream) throws JAXBException {
		
		XMLParser<ItemUserCorrelationRules> parser = xmlParserRegistry.getXMLParser(ItemUserCorrelationRules.class);
		if (parser == null) {
			LOGGER.error("No xml parser found for ItemCorrelationRules");
			throw new IllegalStateException("No xml parser found for ItemCorrelationRules");
		}
		
		ItemUserCorrelationRules rules = parser.unmarshal(xmlInputStream);

		for (ItemUserCorrelationRuleType rule : rules.getItemUserCorrelationRule()) {
			saveItemUserCorrelationRule(rule);
		}
		
	}

	@Override
	public List<ItemCorrelationRuleType> getItemItemCorrelationRulesForTriggerItemQId(String triggerItemQId) {
		
		LOGGER.debug("Finding item item correlation rules for trigger item: {}", triggerItemQId);
		
		List<ItemCorrelationRuleType> rules = new ArrayList<>();
		List<ItemCorrelationRuleDocument> ruleDocs = 
				itemCorrRuleRepo.findByItemCorrelationRuleTriggerItemQualifiedId(triggerItemQId);
		
		if (!ruleDocs.isEmpty()) {
			for (ItemCorrelationRuleDocument ruleDoc : ruleDocs) {
				rules.add(ruleDoc.getItemCorrelationRule());
			}
		}
		
		LOGGER.debug("Item item correlation rule count: {}", rules.size());
		
		return rules;
	}

	@Override
	public List<ItemUserCorrelationRuleType> getItemUserCorrelationRulesForTriggerItemQId(String triggerItemQId) {
		
		LOGGER.debug("Finding item user correlation rules for trigger item: {}", triggerItemQId);
		
		List<ItemUserCorrelationRuleType> rules = new ArrayList<>();
		List<ItemUserCorrelationRuleDocument> ruleDocs = 
				itemUserCorrRuleRepo.findByItemUserCorrelationRuleTriggerItemQualifiedId(triggerItemQId);
		
		if (!ruleDocs.isEmpty()) {
			for (ItemUserCorrelationRuleDocument ruleDoc : ruleDocs) {
				rules.add(ruleDoc.getItemUserCorrelationRule());
			}
		}
		
		LOGGER.debug("Item user correlation rule count: {}", rules.size());
		
		return rules;
	}
	
}
