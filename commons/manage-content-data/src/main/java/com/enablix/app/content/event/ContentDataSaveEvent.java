package com.enablix.app.content.event;

import java.util.Map;

import com.enablix.core.commons.xsdtopojo.ContainerType;

public class ContentDataSaveEvent {

	private Map<String, Object> dataAsMap;
	
	private String templateId;
	
	private ContainerType containerType;
	
	private boolean newRecord;

	public ContentDataSaveEvent(Map<String, Object> dataAsMap, 
			String templateId, ContainerType containerQId, boolean newRecord) {
		super();
		this.dataAsMap = dataAsMap;
		this.templateId = templateId;
		this.containerType = containerQId;
		this.newRecord = newRecord;
	}

	public Map<String, Object> getDataAsMap() {
		return dataAsMap;
	}

	public String getTemplateId() {
		return templateId;
	}

	public ContainerType getContainerType() {
		return containerType;
	}

	public boolean isNewRecord() {
		return newRecord;
	}

}
