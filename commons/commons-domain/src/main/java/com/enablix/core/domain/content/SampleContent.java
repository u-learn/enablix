package com.enablix.core.domain.content;

import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.domain.BaseDocumentEntity;

@Document(collection="ebx_sample_content")
public class SampleContent extends BaseDocumentEntity {

	private String contentQId;
	
	private Map<String, Object> record;
	
	public String getContentQId() {
		return contentQId;
	}

	public void setContentQId(String contentQId) {
		this.contentQId = contentQId;
	}

	public Map<String, Object> getRecord() {
		return record;
	}

	public void setRecord(Map<String, Object> record) {
		this.record = record;
	}
	
}
