package com.enablix.content.approval.model;

import java.util.Map;

import com.enablix.state.change.model.ActionInput;
import com.enablix.state.change.model.RefObject;

public class ContentDetail extends RefObject implements ActionInput {

	private String contentQId;
	
	private String parentIdentity;
	
	private Map<String, Object> data;
	
	private String contentTitle;
	
	private String notes;

	public String getContentQId() {
		return contentQId;
	}

	public void setContentQId(String contentQId) {
		this.contentQId = contentQId;
	}

	public String getParentIdentity() {
		return parentIdentity;
	}

	public void setParentIdentity(String parentIdentity) {
		this.parentIdentity = parentIdentity;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public String getContentTitle() {
		return contentTitle;
	}

	public void setContentTitle(String contentTitle) {
		this.contentTitle = contentTitle;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
}
