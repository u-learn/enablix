package com.enablix.core.ui;

public abstract class ContentPreviewInfo {

	public enum PreviewType {
		IMAGE, IMAGE_BASED_PREVIEW, DOC_NO_PREVIEW, URL, TEXT, NO_PREVIEW, EMBED_HTML
	}
	
	public enum PreviewProperty {
		URL, FILE, TEXT
	}
	
	private PreviewType previewType;

	public ContentPreviewInfo(PreviewType previewType) {
		super();
		this.previewType = previewType;
	}

	public PreviewType getPreviewType() {
		return previewType;
	}

	public void setPreviewType(PreviewType previewType) {
		this.previewType = previewType;
	}
	
}
