package com.enablix.app.content.fetch;

public class FetchContentRequest {

	private String templateId;
	
	private String contentQId;
	
	private String parentRecordIdentity;
	
	private String recordIdentity;

	public FetchContentRequest(String templateId, String contentQId, 
			String parentRecordIdentity, String recordIdentity) {
		super();
		this.templateId = templateId;
		this.contentQId = contentQId;
		this.parentRecordIdentity = parentRecordIdentity;
		this.recordIdentity = recordIdentity;
	}

	public String getTemplateId() {
		return templateId;
	}

	public String getContentQId() {
		return contentQId;
	}

	public String getRecordIdentity() {
		return recordIdentity;
	}

	public String getParentRecordIdentity() {
		return parentRecordIdentity;
	}
	
}
