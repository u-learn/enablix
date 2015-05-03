package com.enablix.app.template.provider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;

import com.enablix.app.template.service.TemplateManager;
import com.enablix.util.data.loader.DataFileProcessor;

public class TemplateFileProcessor implements DataFileProcessor {

	@Autowired
	private TemplateManager templateManager;
	
	@Override
	public void process(File templateFile) {

		FileInputStream fis = null;
		
		try {
			fis = new FileInputStream(templateFile);
			templateManager.saveXml(fis);
			
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
