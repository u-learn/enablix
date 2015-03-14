package com.enablix.app.content;

public class FetchContentRequest {

	private String templateId;
	
	private String contentQId;
	
	private String recordId;

	public FetchContentRequest(String templateId, String contentQId, String recordId) {
		super();
		this.templateId = templateId;
		this.contentQId = contentQId;
		this.recordId = recordId;
	}

	public String getTemplateId() {
		return templateId;
	}

	public String getContentQId() {
		return contentQId;
	}

	public String getRecordId() {
		return recordId;
	}
	
}
