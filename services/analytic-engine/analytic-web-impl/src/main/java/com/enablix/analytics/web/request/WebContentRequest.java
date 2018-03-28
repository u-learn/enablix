package com.enablix.analytics.web.request;

import com.enablix.analytics.context.ContentRequest;

public class WebContentRequest implements ContentRequest {

	private String containerQId;
	
	private String contentIdentity;
	
	private int pageSize;
	
	private int pageNo;

	public WebContentRequest(String containerQId) {
		this(containerQId, null);
	}
	
	public WebContentRequest(String containerQId, String contentIdentity) {
		super();
		this.containerQId = containerQId;
		this.contentIdentity = contentIdentity;
	}
	
	public WebContentRequest() { 
		this(null, null);
	}
	
	public String getContainerQId() {
		return containerQId;
	}

	public void setContainerQId(String containerQId) {
		this.containerQId = containerQId;
	}

	public String getContentIdentity() {
		return contentIdentity;
	}

	public void setContentIdentity(String contentIdentity) {
		this.contentIdentity = contentIdentity;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	
}
