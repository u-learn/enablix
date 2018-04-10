package com.enablix.core.content;

import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.domain.uri.embed.EmbedInfo;

public class EmbeddedUrl {

	private String url;
	
	private String previewImageUrl;
	
	private String type;
	
	private String title;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPreviewImageUrl() {
		return previewImageUrl;
	}

	public void setPreviewImageUrl(String previewImageUrl) {
		this.previewImageUrl = previewImageUrl;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public static EmbeddedUrl fromEmbedInfo(EmbedInfo embedInfo) {
		
		if (embedInfo != null) {
			
			EmbeddedUrl eUrl = new EmbeddedUrl();
			eUrl.setUrl(embedInfo.getUrl());
			
			if (CollectionUtil.isNotEmpty(embedInfo.getImages())) {
				eUrl.setPreviewImageUrl(
						embedInfo.getImages().iterator().next().getUrl());
			}

			eUrl.setType(embedInfo.getOembed() == null ? embedInfo.getType() : embedInfo.getOembed().getType());
			eUrl.setTitle(embedInfo.getTitle());
			
			return eUrl;
		}
	
		return null;
	}

	public static EmbeddedUrl unknownUrl(String url) {
		EmbeddedUrl eUrl = new EmbeddedUrl();
		eUrl.setUrl(url);
		eUrl.setType("unknown");
		return eUrl;
	}
	
}
