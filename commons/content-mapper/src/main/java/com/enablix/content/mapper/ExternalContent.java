package com.enablix.content.mapper;

import java.util.Map;

public class ExternalContent {

	private ContentSource contentSource;
	
	private String contentQId;
	
	private Map<String, Object> data;

	public ExternalContent(ContentSource contentSource, String contentQId, Map<String, Object> data) {
		super();
		this.contentSource = contentSource;
		this.contentQId = contentQId;
		this.data = data;
	}

	public ContentSource getContentSource() {
		return contentSource;
	}

	public void setContentSource(ContentSource externalSourceId) {
		this.contentSource = externalSourceId;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public String getContentQId() {
		return contentQId;
	}

	public void setContentQId(String contentQId) {
		this.contentQId = contentQId;
	}

}
