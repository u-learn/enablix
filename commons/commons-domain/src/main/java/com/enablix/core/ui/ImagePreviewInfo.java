package com.enablix.core.ui;

public class ImagePreviewInfo extends ContentPreviewInfo {

	private String imageUrl;
	
	public ImagePreviewInfo() {
		super(PreviewType.IMAGE);
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
}
