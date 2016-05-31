package com.enablix.app.content.ui.format;

public class DisplayField<V extends FieldValue> {

	private String label;
	
	private V value;

	public DisplayField(String label, V value) {
		super();
		this.label = label;
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "DisplayField [label=" + label + ", value=" + value + "]";
	}
	
}
