package com.enablix.app.content.update;

public class ContentIdentifier {

	private String recordIdentity;
	
	private String contentQId;

	public ContentIdentifier() {
		super();
	}

	public ContentIdentifier(String recordIdentity, String contentQId) {
		super();
		this.recordIdentity = recordIdentity;
		this.contentQId = contentQId;
	}

	public String getRecordIdentity() {
		return recordIdentity;
	}

	public void setRecordIdentity(String recordIdentity) {
		this.recordIdentity = recordIdentity;
	}

	public String getContentQId() {
		return contentQId;
	}

	public void setContentQId(String contentQId) {
		this.contentQId = contentQId;
	}
	
}
