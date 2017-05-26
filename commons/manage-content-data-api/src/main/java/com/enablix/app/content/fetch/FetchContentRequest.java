package com.enablix.app.content.fetch;

import org.springframework.data.domain.Pageable;

public class FetchContentRequest {

	private String templateId;
	
	private String contentQId;
	
	private String parentRecordIdentity;
	
	private String recordIdentity;
	
	private Pageable pageable;

	public FetchContentRequest(String templateId, String contentQId, 
			String parentRecordIdentity, String recordIdentity) {
		this(templateId, contentQId, parentRecordIdentity, recordIdentity, null);
	}
	
	public FetchContentRequest(String templateId, String contentQId, 
			String parentRecordIdentity, String recordIdentity, Pageable pageable) {
		super();
		this.templateId = templateId;
		this.contentQId = contentQId;
		this.parentRecordIdentity = parentRecordIdentity;
		this.recordIdentity = recordIdentity;
		this.pageable = pageable;
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

	public Pageable getPageable() {
		return pageable;
	}
	
}
