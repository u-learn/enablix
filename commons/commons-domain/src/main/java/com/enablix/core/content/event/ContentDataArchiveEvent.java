package com.enablix.core.content.event;

import com.enablix.core.domain.activity.ContentActivity.ContainerType;

public class ContentDataArchiveEvent {

	private String templateId;
	
	private String containerQId;
	
	private String contentIdentity;
	
	private ContainerType containerType;
	
	// is record being archived i.e. value true
	// OR un-archived i.e. value false
	private boolean archived;

	public ContentDataArchiveEvent(String templateId, String containerQId, 
			String contentIdentity, ContainerType containerType, boolean archived) {
		super();
		this.templateId = templateId;
		this.containerQId = containerQId;
		this.contentIdentity = contentIdentity;
		this.containerType = containerType;
		this.archived = archived;
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

	public boolean isArchived() {
		return archived;
	}

	public void setContainerType(ContainerType container) {
		this.containerType = container;
	}
	
}
