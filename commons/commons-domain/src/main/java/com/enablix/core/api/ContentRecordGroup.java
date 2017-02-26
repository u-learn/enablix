package com.enablix.core.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ContentRecordGroup {

	private String contentQId;
	
	private List<Map<String, Object>> records;

	public ContentRecordGroup() {
		this.records = new ArrayList<>();
	}
	
	public String getContentQId() {
		return contentQId;
	}

	public void setContentQId(String contentQId) {
		this.contentQId = contentQId;
	}

	public List<Map<String, Object>> getRecords() {
		return records;
	}

	public void setRecords(List<Map<String, Object>> records) {
		this.records = records;
	}
	
}
