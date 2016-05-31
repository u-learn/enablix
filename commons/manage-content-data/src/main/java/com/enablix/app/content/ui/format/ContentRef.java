package com.enablix.app.content.ui.format;

public class ContentRef implements FieldValue {

	private String textValue;
	private String containerQId;
	private String contentIdentity;

	public String getTextValue() {
		return textValue;
	}

	public void setTextValue(String textValue) {
		this.textValue = textValue;
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

	@Override
	public FieldValueType getValueType() {
		return FieldValueType.CONTENT_REF;
	}

	@Override
	public String toString() {
		return "ContentRef [textValue=" + textValue + ", containerQId=" + containerQId + ", contentIdentity="
				+ contentIdentity + "]";
	}
	
	
}
