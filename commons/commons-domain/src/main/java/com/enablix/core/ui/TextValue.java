package com.enablix.core.ui;

public class TextValue extends AbstractFieldValue {

	private String value;

	public TextValue(String value) {
		super(FieldValueType.TEXT);
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "TextValue [value=" + value + "]";
	}
	
}
