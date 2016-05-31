package com.enablix.app.content.ui.format;

public interface FieldValue {

	public static enum FieldValueType {
		TEXT, DOC, LIST, CONTENT_REF
	}
	
	FieldValueType getValueType();
	
}
