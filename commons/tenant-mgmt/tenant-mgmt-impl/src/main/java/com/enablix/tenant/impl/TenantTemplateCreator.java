package com.enablix.tenant.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.constants.AppConstants;
import com.enablix.commons.util.EnvPropertiesUtil;
import com.enablix.commons.util.IOUtil;
import com.enablix.core.commons.xsd.parser.ContentTemplateXMLParser;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.domain.tenant.Tenant;
import com.enablix.tenant.TenantSetupTask;

@Component
public class TenantTemplateCreator implements TenantSetupTask {

	@Value("${new.tenant.content.template.file:#{'${baseDir}/config/new-tenant-content-template.xml'}}")
	private String templateFile;
	
	@Autowired
	private ContentTemplateXMLParser templateParser;
	
	@Autowired
	private TemplateManager templateMgr;
	
	@PostConstruct
	public void validate() throws Exception {
		parseAndGetTemplate();
	}
	
	@Override
	public void execute(Tenant tenant) throws Exception {
		
		ContentTemplate template = parseAndGetTemplate();
		template.setId(tenant.getDefaultTemplateId());

		templateMgr.save(template);
		
		String templateDestLoc = getTenantTemplateDestination(tenant);
		saveXml(template, templateDestLoc);
	}

	private String getTenantTemplateDestination(Tenant tenant) {
		
		String destFolderLoc = EnvPropertiesUtil.getDataDirectory() 
				+ File.separator + AppConstants.TEMPLATES_DIR 
				+ File.separator + tenant.getTenantId();
		
		File destFolder = new File(destFolderLoc);
		
		if (!destFolder.exists()) {
			destFolder.mkdirs();
		}
		
		return destFolderLoc + File.separator + tenant.getDefaultTemplateId() + ".xml";
	}
	
	private ContentTemplate parseAndGetTemplate() throws FileNotFoundException, JAXBException {
		
		FileInputStream templateXmlInputStream = null;
		
		try {
			templateXmlInputStream = new FileInputStream(new File(templateFile));
			return templateParser.unmarshal(templateXmlInputStream);
			
		} finally {
			IOUtil.closeStream(templateXmlInputStream);
		}
	}
	
	private void saveXml(ContentTemplate template, String location) throws Exception {
		
		File destFile = new File(location);
		
		if (destFile.exists()) {
			destFile.delete();
		}
		
		if (!destFile.createNewFile()) {
			throw new Exception("Unable to create file: " + location);
		}
		
		FileOutputStream fos = null;
		
		try {
			
			fos = new FileOutputStream(destFile);
			templateParser.marshal(template, fos);
			
		} finally {
			IOUtil.closeStream(fos);;
		}
		
	}
	
	@Override
	public float executionOrder() {
		return TenantSetupTaskOrder.TEMPLATE_CREATOR;
	}


}
