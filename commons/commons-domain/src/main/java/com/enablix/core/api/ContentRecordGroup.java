package com.enablix.core.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

public class ContentRecordGroup {

	private String contentQId;
	
	private Page<Map<String, Object>> records;

	public String getContentQId() {
		return contentQId;
	}

	public void setContentQId(String contentQId) {
		this.contentQId = contentQId;
	}

	public Page<Map<String, Object>> getRecords() {
		return records;
	}

	public void setRecords(Page<Map<String, Object>> records) {
		this.records = records;
	}

	public void setRecords(List<Map<String, Object>> contentRecords) {
		setRecords(new PageImpl<>(contentRecords));
	}
	
	public void setRecords(Map<String, Object> contentRecord) {
		
		List<Map<String, Object>> records = new ArrayList<>();
		records.add(contentRecord);
		
		setRecords(new PageImpl<>(records));
	}
	
}
