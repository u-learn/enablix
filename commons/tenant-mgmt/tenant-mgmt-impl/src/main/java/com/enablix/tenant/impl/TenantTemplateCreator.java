package com.enablix.tenant.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.enablix.app.template.service.TemplateManager;
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

		String filename = tenant.getDefaultTemplateId() + ".xml";

		templateMgr.save(template, filename);
		templateMgr.persistOnFilesystem(template, tenant, filename);
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
	
	@Override
	public float executionOrder() {
		return TenantSetupTaskOrder.TEMPLATE_CREATOR;
	}


}
