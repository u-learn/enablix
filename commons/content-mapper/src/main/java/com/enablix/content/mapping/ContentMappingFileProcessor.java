package com.enablix.content.mapping;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.enablix.util.data.loader.DataFileProcessor;

public class ContentMappingFileProcessor implements DataFileProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContentMappingFileProcessor.class);
	
	@Autowired
	private ContentMappingManager mappingsManager;
	
	@Override
	public void process(File mappingFile) {

		FileInputStream fis = null;
		
		try {
			fis = new FileInputStream(mappingFile);
			mappingsManager.saveXml(fis);
			
		} catch (FileNotFoundException | JAXBException e) {
			LOGGER.error("Error loading mapping file: " + mappingFile.getAbsolutePath(), e);
			
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
