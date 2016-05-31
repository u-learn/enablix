package com.enablix.app.content.ui.format;

public class TextValue implements FieldValue {

	private String value;

	public TextValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public FieldValueType getValueType() {
		return FieldValueType.TEXT;
	}

	@Override
	public String toString() {
		return "TextValue [value=" + value + "]";
	}
	
}
