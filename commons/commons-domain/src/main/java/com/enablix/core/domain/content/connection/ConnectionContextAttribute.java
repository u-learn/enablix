package com.enablix.core.domain.content.connection;

import com.enablix.core.api.ContentDataRef;

public class ConnectionContextAttribute {

	private ContentDataRef inContextOfContent;

	public ContentDataRef getInContextOfContent() {
		return inContextOfContent;
	}

	public void setInContextOfContent(ContentDataRef inContextOfContent) {
		this.inContextOfContent = inContextOfContent;
	}
	
}
