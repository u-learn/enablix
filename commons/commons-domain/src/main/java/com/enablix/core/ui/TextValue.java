package com.enablix.core.ui;

import java.util.List;

public class TextValue extends AbstractFieldValue {

	private String value;
	
	private String rawValue;
	
	private List<Hyperlink> links;

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

	public List<Hyperlink> getLinks() {
		return links;
	}

	public void setLinks(List<Hyperlink> links) {
		this.links = links;
	}

	@Override
	public String toString() {
		return "TextValue [value=" + value + "]";
	}
	
}
