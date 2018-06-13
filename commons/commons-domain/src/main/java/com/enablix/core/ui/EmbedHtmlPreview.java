package com.enablix.core.ui;

public class EmbedHtmlPreview extends ContentPreviewInfo {

	private String embedHtml;
	
	public EmbedHtmlPreview() {
		super(PreviewType.EMBED_HTML);
	}

	public String getEmbedHtml() {
		return embedHtml;
	}

	public void setEmbedHtml(String embedHtml) {
		this.embedHtml = embedHtml;
	}

}
