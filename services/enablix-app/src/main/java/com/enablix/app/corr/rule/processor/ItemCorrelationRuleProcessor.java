package com.enablix.app.corr.rule.processor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.enablix.analytics.correlation.rule.ItemCorrelationRuleManager;
import com.enablix.util.data.loader.DataFileProcessor;

public class ItemCorrelationRuleProcessor implements DataFileProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(ItemCorrelationRuleProcessor.class);
	
	@Autowired
	private ItemCorrelationRuleManager corrRuleManager;
	
	@Override
	public void process(File templateFile) {

		FileInputStream fis = null;
		
		try {
			fis = new FileInputStream(templateFile);
			corrRuleManager.saveItemItemCorrelationRuleXml(fis);
			
		} catch (FileNotFoundException | JAXBException e) {
			LOGGER.error("Error loading item-correlation file: " + templateFile.getAbsolutePath(), e);
			
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
