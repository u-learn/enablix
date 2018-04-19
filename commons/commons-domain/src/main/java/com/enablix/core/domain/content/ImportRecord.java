package com.enablix.core.domain.content;

import java.util.Map;

public class ImportRecord {

	private String id;
	
	private String title;
	
	private String contentQId;
	
	private Map<String, Object> sourceRecord;
	
	private Map<String, Object> tags;
	
	private ImportStatus status;
	
	public ImportRecord() {
		this.status = ImportStatus.PENDING;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ImportStatus getStatus() {
		return status;
	}

	public void setStatus(ImportStatus status) {
		this.status = status;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContentQId() {
		return contentQId;
	}

	public void setContentQId(String contentQId) {
		this.contentQId = contentQId;
	}

	public Map<String, Object> getSourceRecord() {
		return sourceRecord;
	}

	public void setSourceRecord(Map<String, Object> sourceRecord) {
		this.sourceRecord = sourceRecord;
	}

	public Map<String, Object> getTags() {
		return tags;
	}

	public void setTags(Map<String, Object> tags) {
		this.tags = tags;
	}
	
}
