package com.enablix.core.ui;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

public class DisplayableContent {

	private List<DisplayField<?>> fields;
	
	private String containerQId;
	private String containerLabel;
	private String recordIdentity;
	private String title;
	private DocRef doc;
	private String accessUrl;

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

	public String getContainerQId() {
		return containerQId;
	}

	public void setContainerQId(String containerQId) {
		this.containerQId = containerQId;
	}

	public String getContainerLabel() {
		return containerLabel;
	}

	public void setContainerLabel(String containerLabel) {
		this.containerLabel = containerLabel;
	}

	public String getRecordIdentity() {
		return recordIdentity;
	}

	public void setRecordIdentity(String recordIdentity) {
		this.recordIdentity = recordIdentity;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAccessUrl() {
		return accessUrl;
	}

	public void setAccessUrl(String accessUrl) {
		this.accessUrl = accessUrl;
	}

	@Override
	public String toString() {
		return "DisplayableContent [fields=" + fields + ", title=" + title + ", doc=" + doc + "]";
	}
	
}
