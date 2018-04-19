package com.enablix.app.content.bulkimport;

import java.util.HashMap;
import java.util.Map;

import com.enablix.core.domain.content.ImportRequest;

public class ImportContext {

	private ImportRequest request;
	
	private Map<String, Object> sharedData;

	public ImportContext(ImportRequest request) {
		this.request = request;
		this.sharedData = new HashMap<>();
	}
	
	public ImportRequest getRequest() {
		return request;
	}

	public Map<String, Object> getSharedData() {
		return sharedData;
	}

	public void setSharedData(Map<String, Object> sharedData) {
		this.sharedData = sharedData;
	}
	
}
