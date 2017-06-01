package com.enablix.content.connection.web;

import java.util.List;

public class ContentValueLinkVO {

	private String recordIdentity;
	
	private String recordTitle;
	
	private List<String> connectedContainers;

	public ContentValueLinkVO() {
		super();
	}

	public ContentValueLinkVO(String recordIdentity, String recordTitle, List<String> connectedContainers) {
		super();
		this.recordIdentity = recordIdentity;
		this.recordTitle = recordTitle;
		this.connectedContainers = connectedContainers;
	}

	public String getRecordIdentity() {
		return recordIdentity;
	}

	public void setRecordIdentity(String recordIdentity) {
		this.recordIdentity = recordIdentity;
	}

	public String getRecordTitle() {
		return recordTitle;
	}

	public void setRecordTitle(String label) {
		this.recordTitle = label;
	}

	public List<String> getConnectedContainers() {
		return connectedContainers;
	}

	public void setConnectedContainers(List<String> connectedContainers) {
		this.connectedContainers = connectedContainers;
	}
	
}
