package com.enablix.core.ui;

public class DisplayField<V extends FieldValue> {

	private String id;
	
	private String label;
	
	private V value;

	public DisplayField(String id, String label, V value) {
		super();
		this.id = id;
		this.label = label;
		this.value = value;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
