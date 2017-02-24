package com.enablix.core.api;

import java.util.ArrayList;
import java.util.List;

public class ContentRecordGroup {

	private String contentQId;
	
	private List<ContentDataRecord> records;

	public ContentRecordGroup() {
		this.records = new ArrayList<>();
	}
	
	public String getContentQId() {
		return contentQId;
	}

	public void setContentQId(String contentQId) {
		this.contentQId = contentQId;
	}

	public List<ContentDataRecord> getRecords() {
		return records;
	}

	public void setRecords(List<ContentDataRecord> records) {
		this.records = records;
	}
	
}
