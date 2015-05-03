package com.enablix.app.content.delete;

import org.springframework.util.Assert;

public class DeleteContentRequest {

	private String templateId;
	
	private String contentQId;
	
	private String recordIdentity;

	public DeleteContentRequest(String templateId, String contentQId, String recordIdentity) {
		
		super();
		
		Assert.hasLength(templateId, "[templateId] should not be null or empty");
		Assert.hasLength(contentQId, "[contentQId] should not be null or empty");
		Assert.hasLength(recordIdentity, "[recordIdentity] should not be null or empty");
		
		this.templateId = templateId;
		this.contentQId = contentQId;
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

}
