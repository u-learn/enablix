package com.enablix.core.api;

import java.util.Map;

import org.springframework.util.Assert;

public class ContentDataRecord {

	private String templateId;

	private String containerQId;

	private Map<String, Object> record;

	private String emailCustomContent;

	public String getEmailCustomContent() {
		return emailCustomContent;
	}

	public void setEmailCustomContent(String emailCustomContent) {
		this.emailCustomContent = emailCustomContent;
	}
	public ContentDataRecord(String templateId, String containerQId, String emailCustomContent,Map<String, Object> record) {

		super();

		Assert.notNull(templateId);
		Assert.notNull(containerQId);
		Assert.notNull(record, "Content record cannot be null");

		this.templateId = templateId;
		this.containerQId = containerQId;
		this.emailCustomContent = emailCustomContent;
		this.record = record;
	}
	public ContentDataRecord(String templateId, String containerQId, Map<String, Object> record) {

		super();

		Assert.notNull(templateId);
		Assert.notNull(containerQId);
		Assert.notNull(record, "Content record cannot be null");

		this.templateId = templateId;
		this.containerQId = containerQId;
		this.record = record;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getContainerQId() {
		return containerQId;
	}

	public void setContainerQId(String containerQId) {
		this.containerQId = containerQId;
	}

	public Map<String, Object> getRecord() {
		return record;
	}

	public void setRecord(Map<String, Object> record) {
		this.record = record;
	}

}
