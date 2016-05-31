package com.enablix.app.content.ui.format;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

public class DisplayableContent {

	private List<DisplayField<?>> fields;
	
	private DocRef doc;

	public DisplayableContent() {
		this.fields = new ArrayList<>();
	}
	
	public List<DisplayField<?>> getFields() {
		return fields;
	}

	public void setFields(List<DisplayField<?>> fields) {
		Assert.notNull(fields, "Field list can not be null");
		this.fields = fields;
	}

	public DocRef getDoc() {
		return doc;
	}

	public void setDoc(DocRef doc) {
		this.doc = doc;
	}

	public void addField(DisplayField<?> field) {
		fields.add(field);
	}

	@Override
	public String toString() {
		return "DisplayableContent [fields=" + fields + ", doc=" + doc + "]";
	}
	
}
