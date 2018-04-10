package com.enablix.app.template.service;

import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;

public class ContainerAndTemplate {
	
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