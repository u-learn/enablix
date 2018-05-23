package com.enablix.core.ui;

public class RichTextValue extends TextValue {

	public RichTextValue(String value) {
		super(value);
	}
	
	public RichTextValue(String rawValue, String value) {
		super(FieldValueType.RICH_TEXT, rawValue, value);
	}

}
