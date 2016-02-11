package com.enablix.app.content.event;

public class ContentDataDelEvent {

	private String templateId;
	
	private String containerQId;
	
	private String contentIdentity;

	public ContentDataDelEvent(String templateId, String containerQId, String contentIdentity) {
		super();
		this.templateId = templateId;
		this.containerQId = containerQId;
		this.contentIdentity = contentIdentity;
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
	
}
