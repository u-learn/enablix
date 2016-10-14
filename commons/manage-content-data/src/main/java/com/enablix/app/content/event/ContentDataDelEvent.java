package com.enablix.app.content.event;

import com.enablix.core.commons.xsdtopojo.ContainerType;

public class ContentDataDelEvent {

	private String templateId;
	
	private String containerQId;
	
	private String contentIdentity;
	
	private String contentTitle;
	
	private ContainerType containerType;

	public ContentDataDelEvent(String templateId, String containerQId, 
			String contentIdentity, ContainerType containerType, String contentTitle) {
		super();
		this.templateId = templateId;
		this.containerQId = containerQId;
		this.contentIdentity = contentIdentity;
		this.containerType = containerType;
	}

	public String getTemplateId() {
		return templateId;
	}

	public String getContainerQId() {
		return containerQId;
	}

	public String getContentIdentity() {
		return contentIdentity;
	}

	public ContainerType getContainerType() {
		return containerType;
	}

	public String getContentTitle() {
		return contentTitle;
	}

	public void setContainerType(ContainerType container) {
		this.containerType = container;
	}
	
}
