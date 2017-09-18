package com.enablix.core.domain.content.quality;

public class ContentAttribute extends AlertInfo {

	private String contentQId;
	
	private String attributeId;

	@SuppressWarnings("unused")
	private ContentAttribute() {
		// for ORM
	}
	
	public ContentAttribute(String contentQId, String attributeId) {
		super();
		this.contentQId = contentQId;
		this.attributeId = attributeId;
	}

	public String getContentQId() {
		return contentQId;
	}

	public void setContentQId(String contentQId) {
		this.contentQId = contentQId;
	}

	public String getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(String attributeId) {
		this.attributeId = attributeId;
	}
	
}
