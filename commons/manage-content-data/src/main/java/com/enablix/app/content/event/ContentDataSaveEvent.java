package com.enablix.app.content.event;

import java.util.Map;

import com.enablix.core.commons.xsdtopojo.ContainerType;

public class ContentDataSaveEvent {

	private Map<String, Object> dataAsMap;
	
	private String templateId;
	
	private ContainerType containerType;
	
	private boolean newRecord;
	
	// Old data before the record update. Only applicable when
	// update action was taken i.e. newRecord == false
	private Map<String, Object> priorData; 

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

	public Map<String, Object> getPriorData() {
		return priorData;
	}

	public void setPriorData(Map<String, Object> priorData) {
		this.priorData = priorData;
	}

}
