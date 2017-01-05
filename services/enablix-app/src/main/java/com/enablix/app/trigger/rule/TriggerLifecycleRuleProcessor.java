package com.enablix.app.trigger.rule;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.enablix.trigger.lifecycle.rule.TriggerLifecycleRuleManager;
import com.enablix.util.data.loader.DataFileProcessor;

public class TriggerLifecycleRuleProcessor implements DataFileProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(TriggerLifecycleRuleProcessor.class);
	
	@Autowired
	private TriggerLifecycleRuleManager triggerLifecycleRuleMgr;
	
	@Override
	public void process(File templateFile) {

		FileInputStream fis = null;
		
		try {
			fis = new FileInputStream(templateFile);
			triggerLifecycleRuleMgr.saveTriggerLifecycleRuleXml(fis);
			
		} catch (FileNotFoundException | JAXBException e) {
			LOGGER.error("Error loading trigger file: " + templateFile.getName(), e);
			
		} finally {
			
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception e) {
					// ignore
				}
			}
		}
	}
	
}
