package com.enablix.core.domain.uri.embed;

public class Favicon {

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
		return "Favicon [url=" + url + ", safe=" + safe + "]";
	}
	
}
