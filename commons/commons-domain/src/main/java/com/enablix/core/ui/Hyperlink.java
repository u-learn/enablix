package com.enablix.core.ui;

import com.enablix.core.content.EmbeddedUrl;

public class Hyperlink {

	private String href;
	
	private String title;
	
	private String rawText;
	
	private String thumbnailUrl;

	public Hyperlink(String href, String title, String rawText) {
		super();
		this.href = href;
		this.title = title;
		this.rawText = rawText;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRawText() {
		return rawText;
	}

	public void setRawText(String rawText) {
		this.rawText = rawText;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public static Hyperlink fromEmbeddedUrl(EmbeddedUrl embeddedUrl) {
		Hyperlink link = new Hyperlink(embeddedUrl.getUrl(), embeddedUrl.getTitle(), embeddedUrl.getTitle());
		link.setThumbnailUrl(embeddedUrl.getPreviewImageUrl());
		return link;
	}
	
}
