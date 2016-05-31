package com.enablix.app.trigger.rule;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;

import com.enablix.trigger.lifecycle.rule.TriggerLifecycleRuleManager;
import com.enablix.util.data.loader.DataFileProcessor;

public class TriggerLifecycleRuleProcessor implements DataFileProcessor {

	@Autowired
	private TriggerLifecycleRuleManager triggerLifecycleRuleMgr;
	
	@Override
	public void process(File templateFile) {

		FileInputStream fis = null;
		
		try {
			fis = new FileInputStream(templateFile);
			triggerLifecycleRuleMgr.saveTriggerLifecycleRuleXml(fis);
			
		} catch (FileNotFoundException | JAXBException e) {
			e.printStackTrace();
			
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
