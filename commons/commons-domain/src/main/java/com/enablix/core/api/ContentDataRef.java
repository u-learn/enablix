package com.enablix.core.api;

public class ContentDataRef {

	private String templateId;
	
	private String containerQId;
	
	private String instanceIdentity;

	protected ContentDataRef() {
		// for JSON conversions
	}
	
	public ContentDataRef(String templateId, String containerQId, String instanceIdentity) {
		super();
		this.templateId = templateId;
		this.containerQId = containerQId;
		this.instanceIdentity = instanceIdentity;
	}

	public String getTemplateId() {
		return templateId;
	}

	public String getContainerQId() {
		return containerQId;
	}

	public String getInstanceIdentity() {
		return instanceIdentity;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public void setContainerQId(String containerQId) {
		this.containerQId = containerQId;
	}

	public void setInstanceIdentity(String instanceIdentity) {
		this.instanceIdentity = instanceIdentity;
	}
	
}
