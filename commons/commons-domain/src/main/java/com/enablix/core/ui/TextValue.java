package com.enablix.core.ui;

public class TextValue extends AbstractFieldValue {

	private String value;
	
	private String rawValue;

	public TextValue(String value) {
		this(value, value);
	}
	
	public TextValue(String rawValue, String value) {
		super(FieldValueType.TEXT);
		this.rawValue = rawValue;
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getRawValue() {
		return rawValue;
	}

	@Override
	public String toString() {
		return "TextValue [value=" + value + "]";
	}
	
}
