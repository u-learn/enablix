package com.enablix.core.api;

import java.util.List;

public class LinkedContent<D> implements Comparable<LinkedContent<D>> {
	
	private String contentQId;
	private String contentLabel;
	private boolean selected;
	private int contentCount;
	private List<D> records;
	
	public String getContentQId() {
		return contentQId;
	}
	
	public void setContentQId(String contentQId) {
		this.contentQId = contentQId;
	}
	
	public String getContentLabel() {
		return contentLabel;
	}
	
	public void setContentLabel(String contentLabel) {
		this.contentLabel = contentLabel;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public int getContentCount() {
		return contentCount;
	}

	public void setContentCount(int contentCount) {
		this.contentCount = contentCount;
	}

	public List<D> getRecords() {
		return records;
	}
	
	public void setRecords(List<D> records) {
		this.records = records;
	}
	
	@Override
	public int compareTo(LinkedContent<D> o) {
		return contentLabel.compareTo(o.contentLabel);
	}
	
}