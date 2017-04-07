package com.enablix.core.domain.segment;

import java.util.HashMap;
import java.util.Map;

public class DataSegmentInfo {

	private Map<String, Object> attributes;
	
	public DataSegmentInfo() {
		this.attributes = new HashMap<>();
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public void addAttribute(String dsAttrId, Object object) {
		attributes.put(dsAttrId, object);
	}
	
}
