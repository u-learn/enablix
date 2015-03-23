package com.enablix.app.content.update;

import java.util.Map;

import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.json.JsonUtil;

public class UpdateContentRequest implements ContentUpdateContext {

	private String templateId;
	
	private String parentIdentity;
	
	private String contentQId;
	
	private String jsonData;
	
	private Map<String, Object> dataAsMap;

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
		this.parentIdentity = recordId;
		this.contentQId = contentQId;
		this.jsonData = jsonData;
		this.dataAsMap = JsonUtil.jsonToMap(jsonData);
	}

	public String getTemplateId() {
		return templateId;
	}

	public String getParentIdentity() {
		return parentIdentity;
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

	public void setParentIdentity(String recordId) {
		this.parentIdentity = recordId;
	}

	public void setContentQId(String contentQId) {
		this.contentQId = contentQId;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

	public Map<String, Object> getDataAsMap() {
		return dataAsMap;
	}

	public boolean isUpdateAttribRequest() {
		return dataAsMap.containsKey(ContentDataConstants.IDENTITY_KEY);
	}
	
	public boolean isInsertRootRequest() {
		return !isUpdateAttribRequest() && StringUtil.isEmpty(parentIdentity);
	}
	
	public boolean isInsertChildRequest() {
		return !isUpdateAttribRequest() && !StringUtil.isEmpty(parentIdentity);
	}

	@Override
	public String parentIdentity() {
		return getParentIdentity();
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
