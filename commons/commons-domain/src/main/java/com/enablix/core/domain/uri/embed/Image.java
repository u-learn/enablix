package com.enablix.core.domain.uri.embed;

public class Image {

	private String url;
	
	private String safe;
	
	private int width;
	
	private int height;

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

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public String toString() {
		return "Image [url=" + url + ", safe=" + safe + ", width=" + width + ", height=" + height + "]";
	}
	
}
