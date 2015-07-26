package com.enablix.analytics.web.request;

import com.enablix.analytics.context.ContentRequest;

public class WebContentRequest implements ContentRequest {

	private String containerQId;
	
	private String contentIdentity;

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

	
	
}
