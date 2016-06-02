package com.enablix.app.content.ui.format;

public abstract class AbstractFieldValue implements FieldValue {

	private FieldValueType valueType;
	
	public AbstractFieldValue(FieldValueType valueType) {
		this.valueType = valueType;
	}
	
	@Override
	public FieldValueType getValueType() {
		return valueType;
	}

}
