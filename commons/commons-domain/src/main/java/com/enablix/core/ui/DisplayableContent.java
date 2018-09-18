package com.enablix.core.ui;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

public class DisplayableContent {

	private List<DisplayField<?>> fields;
	
	private String containerQId;
	private String containerLabel;
	private String singularContainerLabel;
	private String recordIdentity;
	private String title;
	private DocRef doc;
	private Hyperlink hyperlink;
	private RichTextValue richText;
	private String portalUrl;
	
	private ContentPreviewInfo preview;

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

	public String getSingularContainerLabel() {
		return singularContainerLabel;
	}

	public void setSingularContainerLabel(String singularContainerLabel) {
		this.singularContainerLabel = singularContainerLabel;
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
	
	public RichTextValue getRichText() {
		return richText;
	}

	public void setRichText(RichTextValue richText) {
		this.richText = richText;
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

	public Hyperlink getHyperlink() {
		return hyperlink;
	}

	public void setHyperlink(Hyperlink hyperlink) {
		this.hyperlink = hyperlink;
	}

	public String getPortalUrl() {
		return portalUrl;
	}

	public void setPortalUrl(String accessUrl) {
		this.portalUrl = accessUrl;
	}
	
	public ContentPreviewInfo getPreview() {
		return preview;
	}

	public void setPreview(ContentPreviewInfo preview) {
		this.preview = preview;
	}

	@Override
	public String toString() {
		return "DisplayableContent [fields=" + fields + ", containerQId=" + containerQId + ", containerLabel="
				+ containerLabel + ", recordIdentity=" + recordIdentity + ", title=" + title + ", doc=" + doc
				+ ", hyperlink=" + hyperlink + ", portalUrl=" + portalUrl + "]";
	}

	
	
}
