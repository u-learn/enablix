package com.enablix.core.ui;

import java.util.List;

public class ImagesBasedPreviewInfo extends ContentPreviewInfo {

	private List<String> imageUrls;
	
	public ImagesBasedPreviewInfo() {
		super(PreviewType.IMAGE_BASED_PREVIEW);
	}

	public List<String> getImageUrls() {
		return imageUrls;
	}

	public void setImageUrls(List<String> imageUrls) {
		this.imageUrls = imageUrls;
	}
	
}
