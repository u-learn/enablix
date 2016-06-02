package com.enablix.app.content.ui.format;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

public class ListValue extends AbstractFieldValue {

	private List<FieldValue> valueList;
	
	public ListValue() {
		super(FieldValueType.LIST);
		valueList = new ArrayList<>();
	}
	
	public List<FieldValue> getValueList() {
		return valueList;
	}

	public void setValueList(List<FieldValue> valueList) {
		Assert.notNull(valueList, "Value list cannot be null");
		this.valueList = valueList;
	}
	
	public void addValue(FieldValue value) {
		valueList.add(value);
	}

	@Override
	public String toString() {
		return "ListValue [valueList=" + valueList + "]";
	}

}
