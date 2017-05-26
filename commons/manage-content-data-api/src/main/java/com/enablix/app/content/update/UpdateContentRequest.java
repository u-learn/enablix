package com.enablix.app.content.update;

import java.util.Map;

import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.json.JsonUtil;

public class UpdateContentRequest implements ContentUpdateContext {

	private String templateId;
	
	private String parentIdentity;
	
	private String contentQId;
	
	private Map<String, Object> dataAsMap;

	protected UpdateContentRequest() {
		
	}
	
	public UpdateContentRequest(String templateId, 
			String contentQId, String jsonData) {
		this(templateId, null, contentQId, jsonData);
	}
	
	public UpdateContentRequest(String templateId, 
			String parentIdentity, String contentQId, String jsonData) {
		this(templateId, parentIdentity, contentQId, JsonUtil.jsonToMap(jsonData));
	}

	public UpdateContentRequest(String templateId, 
			String parentIdentity, String contentQId, Map<String, Object> dataAsMap) {
		super();
		this.templateId = templateId;
		this.parentIdentity = parentIdentity;
		this.contentQId = contentQId;
		this.dataAsMap = dataAsMap;
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

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public void setParentIdentity(String recordId) {
		this.parentIdentity = recordId;
	}

	public void setContentQId(String contentQId) {
		this.contentQId = contentQId;
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
