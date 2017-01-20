package com.enablix.core.api;

import java.util.Map;

import org.springframework.util.Assert;

public class ContentDataRecord {

	private String templateId;

	private String containerQId;

	private Map<String, Object> record;

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
