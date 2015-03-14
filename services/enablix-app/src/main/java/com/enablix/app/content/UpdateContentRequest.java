package com.enablix.app.content;

import com.enablix.commons.util.StringUtil;

public class UpdateContentRequest {

	private String templateId;
	
	private String recordId;
	
	private String contentQId;
	
	private String jsonData;

	public UpdateContentRequest(String templateId, 
			String contentQId, String jsonData) {
		this(templateId, null, contentQId, jsonData);
	}
	
	public UpdateContentRequest(String templateId, 
			String recordId, String contentQId, String jsonData) {
		super();
		this.templateId = templateId;
		this.recordId = recordId;
		this.contentQId = contentQId;
		this.jsonData = jsonData;
	}

	public String getTemplateId() {
		return templateId;
	}

	public String getRecordId() {
		return recordId;
	}

	public String getContentQId() {
		return contentQId;
	}

	public String getJsonData() {
		return jsonData;
	}

	public boolean isNewRecord() {
		return StringUtil.isEmpty(getRecordId());
	}
	
}
