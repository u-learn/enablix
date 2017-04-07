package com.enablix.app.template.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.concurrent.Events;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsd.parser.XMLParser;
import com.enablix.core.commons.xsd.parser.XMLParserRegistry;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.commons.xsdtopojo.DataDefinitionType;
import com.enablix.core.commons.xsdtopojo.UiDefinitionType;
import com.enablix.core.domain.content.TemplateDocument;
import com.enablix.core.mq.Event;
import com.enablix.core.mq.util.EventUtil;
import com.enablix.services.util.TemplateUtil;
import com.enablix.services.util.template.TemplateWrapper;

@Service
public class TemplateManagerImpl implements TemplateManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(TemplateManagerImpl.class); 
	
	@Autowired
	private TemplateCrudService crudService;
	
	@Autowired
	private XMLParserRegistry xmlParserRegistry;
	
	@Autowired
	private TemplateVersionManager templateVersionManager;
	
	@Autowired
	private TemplateCache templateCache;
	
	@Override
	public void save(ContentTemplate template) {

		TemplateDocument templateDoc = new TemplateDocument();
		templateDoc.setTemplate(template);
		templateDoc.setIdentity(template.getId());
		
		crudService.saveOrUpdate(templateDoc);
		
		updateTemplateInCache(template);
		templateVersionManager.updateTemplateVersion(template);
		
		EventUtil.publishEvent(new Event<ContentTemplate>(Events.CONTENT_TEMPLATE_UPDATED, template));
	}
	
	private void updateTemplateInCache(ContentTemplate template) {
		updateTemplateInCache(new TemplateWrapper(template));
	}
	
	private void updateTemplateInCache(TemplateFacade templateWrapper) {
		templateCache.put(ProcessContext.get().getTenantId(), templateWrapper);
	}
	
	@Override
	public ContentTemplate getTemplate(String templateId) {
		TemplateFacade templateWrapper = getTemplateFacade(templateId);
		return templateWrapper == null ? null : templateWrapper.getTemplate();
	}
	
	@Override
	public TemplateFacade getTemplateFacade(String templateId) {
		
		TemplateFacade templateFacade = templateCache.getTemplate(ProcessContext.get().getTenantId(), templateId);
		
		if (templateFacade == null) {
		
			TemplateDocument templateDoc = crudService.findByIdentity(templateId);
			if (templateDoc != null) {
				templateFacade = new TemplateWrapper(templateDoc.getTemplate());
				updateTemplateInCache(templateFacade);
			}
			
		} 
		
		return templateFacade;
	}

	@Override
	public DataDefinitionType getDataDefinition(String templateId) {
		ContentTemplate template = getTemplate(templateId);
		return template == null ? null : template.getDataDefinition();
	}

	@Override
	public UiDefinitionType getUIDefinition(String templateId) {
		ContentTemplate template = getTemplate(templateId);
		return template == null ? null : template.getUiDefinition();
	}

	@Override
	public void saveXml(InputStream templateXmlInputStream) throws JAXBException {
		
		XMLParser<ContentTemplate> parser = xmlParserRegistry.getXMLParser(ContentTemplate.class);
		if (parser == null) {
			LOGGER.error("No xml parser found for ContentTemplate");
			throw new IllegalStateException("No xml parser found for ContentTemplate");
		}
		
		ContentTemplate template = parser.unmarshal(templateXmlInputStream);

		save(template);
	}

	@Override
	public List<ContainerType> getChildContainers(String templateId, String parentQId) {
		
		ContentTemplate template = getTemplate(templateId);
		
		if (template == null) {
			LOGGER.error("Invalid template id : {}", templateId);
			throw new IllegalArgumentException("Invalid template id [" + templateId + "]");
		}
		
		List<ContainerType> containers = new ArrayList<>();
		
		if (StringUtil.isEmpty(parentQId)) {
			containers = template.getDataDefinition().getContainer();
			
		} else {
			ContainerType parentContainer = 
					TemplateUtil.findContainer(template.getDataDefinition(), parentQId);
			containers = parentContainer.getContainer();
		}
		
		return containers;
	}

}
