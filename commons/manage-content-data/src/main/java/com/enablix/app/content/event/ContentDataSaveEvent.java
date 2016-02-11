package com.enablix.app.content.event;

import java.util.Map;

public class ContentDataSaveEvent {

	private Map<String, Object> dataAsMap;
	
	private String templateId;
	
	private String containerQId;
	
	private boolean newRecord;

	public ContentDataSaveEvent(Map<String, Object> dataAsMap, 
			String templateId, String containerQId, boolean newRecord) {
		super();
		this.dataAsMap = dataAsMap;
		this.templateId = templateId;
		this.containerQId = containerQId;
		this.newRecord = newRecord;
	}

	public Map<String, Object> getDataAsMap() {
		return dataAsMap;
	}

	public String getTemplateId() {
		return templateId;
	}

	public String getContainerQId() {
		return containerQId;
	}

	public boolean isNewRecord() {
		return newRecord;
	}

}
