package com.enablix.app.content.update;

import com.enablix.commons.util.StringUtil;

public class UpdateContentRequest implements ContentUpdateContext {

	private String templateId;
	
	private String recordId;
	
	private String contentQId;
	
	private String jsonData;

	protected UpdateContentRequest() {
		
	}
	
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

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public void setContentQId(String contentQId) {
		this.contentQId = contentQId;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

	public boolean isNewRecord() {
		return StringUtil.isEmpty(getRecordId());
	}

	@Override
	public String recordId() {
		return getRecordId();
	}

	@Override
	public String contentQId() {
		return getContentQId();
	}

	@Override
	public String templateId() {
		return getTemplateId();
	}
	
}
