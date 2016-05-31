package com.enablix.trigger.lifecycle.rule.impl;

import java.io.InputStream;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.core.commons.xsd.parser.XMLParser;
import com.enablix.core.commons.xsd.parser.XMLParserRegistry;
import com.enablix.core.commons.xsdtopojo.ContentTriggerDefType;
import com.enablix.core.commons.xsdtopojo.TriggersDef;
import com.enablix.core.domain.trigger.TriggerLifecycleRule;
import com.enablix.trigger.lifecycle.rule.TriggerLifecycleRuleManager;

@Component
public class TriggerLifecycleRuleManagerImpl implements TriggerLifecycleRuleManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(TriggerLifecycleRuleManagerImpl.class);
	
	@Autowired
	private TriggerLifecycleRuleCrudService crudService;
	
	@Autowired
	private XMLParserRegistry xmlParserRegistry;
	
	@Override
	public void saveTriggerLifecycleRule(ContentTriggerDefType contentTrigger) {
		TriggerLifecycleRule rule = new TriggerLifecycleRule();
		rule.setId(contentTrigger.getId());
		rule.setContentTriggerRule(contentTrigger);
		crudService.saveOrUpdate(rule);
	}

	@Override
	public void saveTriggerLifecycleRuleXml(InputStream xmlInputStream) throws JAXBException {
		XMLParser<TriggersDef> parser = xmlParserRegistry.getXMLParser(TriggersDef.class);
		if (parser == null) {
			LOGGER.error("No xml parser found for Triggers");
			throw new IllegalStateException("No xml parser found for Triggers");
		}
		
		TriggersDef rules = parser.unmarshal(xmlInputStream);

		for (ContentTriggerDefType rule : rules.getContentTriggerDef()) {
			saveTriggerLifecycleRule(rule);
		}
		
	}

}
