package com.enablix.app.template.service;

import java.io.InputStream;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enablix.core.commons.xsd.parser.XMLParser;
import com.enablix.core.commons.xsd.parser.XMLParserRegistry;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.commons.xsdtopojo.DataDefinitionType;
import com.enablix.core.commons.xsdtopojo.UiDefinitionType;
import com.enablix.core.domain.content.TemplateDocument;

@Service
public class TemplateManagerImpl implements TemplateManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(TemplateManagerImpl.class); 
	
	@Autowired
	private TemplateCrudService crudService;
	
	@Autowired
	private XMLParserRegistry xmlParserRegistry;
	
	@Override
	public void save(ContentTemplate template) {
		TemplateDocument templateDoc = new TemplateDocument();
		templateDoc.setTemplate(template);
		templateDoc.setIdentity(template.getId());
		crudService.saveOrUpdate(templateDoc);
	}
	
	@Override
	public ContentTemplate getTemplate(String templateId) {
		TemplateDocument template = crudService.findByIdentity(templateId);
		return template == null ? null : template.getTemplate();
	}

	@Override
	public DataDefinitionType getDataDefinition(String templateId) {
		TemplateDocument template = crudService.findByIdentity(templateId);
		return template == null ? null : template.getTemplate().getDataDefinition();
	}

	@Override
	public UiDefinitionType getUIDefinition(String templateId) {
		TemplateDocument template = crudService.findByIdentity(templateId);
		return template == null ? null : template.getTemplate().getUiDefinition();
	}

	@Override
	public void saveXml(InputStream templateXmlInputStream) throws JAXBException {
		
		XMLParser parser = xmlParserRegistry.getXMLParser(ContentTemplate.class);
		if (parser == null) {
			LOGGER.error("No xml parser found for ContentTemplate");
			throw new IllegalStateException("No xml parser found for ContentTemplate");
		}
		
		Unmarshaller unmarshaller = parser.getUnmarshaller();
		ContentTemplate template = (ContentTemplate) unmarshaller.unmarshal(templateXmlInputStream);

		save(template);
	}

}
