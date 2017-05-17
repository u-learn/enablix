package com.enablix.commons.dms.api;

import java.util.List;

import com.enablix.core.api.DocInfo;

public class ImagePreview extends DocPreviewData {

	private List<DocInfo> parts;
	
	public ImagePreview() {
		super(PreviewType.IMAGE);
	}

	public void setParts(List<DocInfo> images) {
		this.parts = images;
	}

	@Override
	public List<DocInfo> getParts() {
		return parts;
	}
	
}
