package com.enablix.core.ui;

public interface FieldValue {

	public static enum FieldValueType {
		TEXT, DOC, LIST, CONTENT_REF
	}
	
	FieldValueType getValueType();
	
}
