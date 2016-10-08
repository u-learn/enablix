package com.enablix.content.mapper;

import java.util.Map;

import com.enablix.core.api.ContentDataRecord;

public class EnablixContent {

	private ContentDataRecord dataRecord;
	
	private String parentIdentity;

	public EnablixContent(Map<String, Object> data, String contentQId, String templateId) {
		super();
		this.dataRecord = new ContentDataRecord(templateId, contentQId, data);
	}

	public Map<String, Object> getData() {
		return dataRecord.getRecord();
	}

	public ContentDataRecord getDataRecord() {
		return dataRecord;
	}

	public String getParentIdentity() {
		return parentIdentity;
	}

	public void setParentIdentity(String parentIdentity) {
		this.parentIdentity = parentIdentity;
	}

}
