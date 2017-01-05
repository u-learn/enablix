package com.enablix.app.play.definition;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.enablix.play.definition.PlayDefinitionManager;
import com.enablix.util.data.loader.DataFileProcessor;

public class PlayDefinitionProcessor implements DataFileProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(PlayDefinitionProcessor.class);
	
	@Autowired
	private PlayDefinitionManager playDefinitionMgr;
	
	@Override
	public void process(File templateFile) {

		FileInputStream fis = null;
		
		try {
			fis = new FileInputStream(templateFile);
			playDefinitionMgr.savePlayTemplateXml(fis);
			
		} catch (FileNotFoundException | JAXBException e) {
			LOGGER.error("Error loading play template file: " + templateFile.getAbsolutePath(), e);
			
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
