package com.enablix.core.domain.uri.embed;

public class Audio {

	private String url;
	
	private String safe;

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

	@Override
	public String toString() {
		return "Audio [url=" + url + ", safe=" + safe + "]";
	}
	
}
