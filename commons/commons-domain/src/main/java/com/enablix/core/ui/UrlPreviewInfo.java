package com.enablix.core.ui;

import com.enablix.core.domain.uri.embed.EmbedInfo;

public class UrlPreviewInfo extends ContentPreviewInfo {

	private EmbedInfo embedInfo;
	
	public UrlPreviewInfo() {
		super(PreviewType.URL);
	}

	public EmbedInfo getEmbedInfo() {
		return embedInfo;
	}

	public void setEmbedInfo(EmbedInfo embedInfo) {
		this.embedInfo = embedInfo;
	}

}
