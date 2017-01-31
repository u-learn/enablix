package com.enablix.app.template.service;

import java.io.InputStream;
import java.util.List;

import javax.xml.bind.JAXBException;

import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.commons.xsdtopojo.DataDefinitionType;
import com.enablix.core.commons.xsdtopojo.UiDefinitionType;
import com.enablix.services.util.template.TemplateWrapper;

public interface TemplateManager {

	void save(ContentTemplate template);
	
	ContentTemplate getTemplate(String templateId);
	
	DataDefinitionType getDataDefinition(String templateId);
	
	UiDefinitionType getUIDefinition(String templateId);

	void saveXml(InputStream templateXmlInputStream) throws JAXBException;
	
	List<ContainerType> getChildContainers(String templateId, String parentQId);

	TemplateWrapper getTemplateWrapper(String templateId);
	
}
