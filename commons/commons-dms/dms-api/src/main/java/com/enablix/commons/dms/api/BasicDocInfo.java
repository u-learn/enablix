package com.enablix.commons.dms.api;

import java.util.HashMap;
import java.util.Map;

import com.enablix.core.api.DocInfo;

public class BasicDocInfo implements DocInfo {

	private String name;
	
	private String location;
	
	private long contentLength;
	
	private String contentType;
	
	private Map<String, Object> properties = new HashMap<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public long getContentLength() {
		return contentLength;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void addProperties(Map<String, Object> docProps) {
		properties.putAll(docProps);
	}

	public Map<String, Object> getProperties() {
		return properties;
	}
	
}
