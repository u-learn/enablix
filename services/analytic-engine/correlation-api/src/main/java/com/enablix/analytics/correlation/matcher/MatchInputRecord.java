package com.enablix.analytics.correlation.matcher;

import java.util.Map;

public class MatchInputRecord {

	private Map<String, Object> record;
	
	private String contentQId;
	
	private String triggerItemQId;
	
	private MatchInputRecord parent;

	public MatchInputRecord(String triggerItemQId, String contentQId, Map<String, Object> record) {
		super();
		this.triggerItemQId = triggerItemQId;
		this.contentQId = contentQId;
		this.record = record;
	}

	public Map<String, Object> getRecord() {
		return record;
	}

	public void setRecord(Map<String, Object> record) {
		this.record = record;
	}

	public String getContentQId() {
		return contentQId;
	}

	public void setContentQId(String contentQId) {
		this.contentQId = contentQId;
	}

	public String getTriggerItemQId() {
		return triggerItemQId;
	}

	public void setTriggerItemQId(String triggerItemQId) {
		this.triggerItemQId = triggerItemQId;
	}

	public MatchInputRecord getParent() {
		return parent;
	}

	public void setParent(MatchInputRecord parent) {
		this.parent = parent;
	}
	
}
