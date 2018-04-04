package com.enablix.app.template.service;

import java.io.InputStream;
import java.util.List;

import javax.xml.bind.JAXBException;

import com.enablix.commons.exception.ValidationException;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.commons.xsdtopojo.DataDefinitionType;
import com.enablix.core.commons.xsdtopojo.UiDefinitionType;
import com.enablix.core.domain.tenant.Tenant;

@SuppressWarnings("restriction")
public interface TemplateManager {

	void save(ContentTemplate template, String filename);
	
	ContentTemplate getTemplate(String templateId);
	
	DataDefinitionType getDataDefinition(String templateId);
	
	UiDefinitionType getUIDefinition(String templateId);

	void saveXml(InputStream templateXmlInputStream, String filename) throws JAXBException;
	
	List<ContainerType> getChildContainers(String templateId, String parentQId);

	TemplateFacade getTemplateFacade(String templateId);
	
	ContentTemplate updateContainer(String templateId, ContainerType container) throws ValidationException, Exception;

	void persistOnFilesystem(ContentTemplate template, Tenant tenant, String filename) throws Exception;

	ContainerAndTemplate addContainer(String templateId, ContainerType container) throws Exception;
	
	ContainerAndTemplate deleteContainer(String templateId, String containerQId) throws Exception;
	
	public static class ContainerAndTemplate {
		
		private ContainerType container;
		
		private ContentTemplate template;
		
		public ContainerAndTemplate() {}
		
		public ContainerAndTemplate(ContainerType container, ContentTemplate template) {
			super();
			this.container = container;
			this.template = template;
		}

		public ContainerType getContainer() {
			return container;
		}
		
		public void setContainer(ContainerType container) {
			this.container = container;
		}
		
		public ContentTemplate getTemplate() {
			return template;
		}
		
		public void setTemplate(ContentTemplate template) {
			this.template = template;
		}
		
		
	}
	
}
