package com.enablix.core.domain.uri.embed;

public class Video {

	private String url;
	
	private String safe;
	
	private String type;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSafe() {
		return safe;
	}

	public void setSafe(String safe) {
		this.safe = safe;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Video [url=" + url + ", safe=" + safe + ", type=" + type + "]";
	}
	
}
