package com.enablix.trigger.lifecycle.rule;

import java.io.InputStream;

import javax.xml.bind.JAXBException;

import com.enablix.core.commons.xsdtopojo.ContentTriggerDefType;

public interface TriggerLifecycleRuleManager {

	void saveTriggerLifecycleRule(ContentTriggerDefType contentTrigger);
	
	void saveTriggerLifecycleRuleXml(InputStream xmlInputStream) throws JAXBException;
	
}
