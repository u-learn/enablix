package com.enablix.doc.preview;

public enum ThumbnailSize {

	SMALL(320, "st"), LARGE(-1, "lt"), ICON(120, "ic");
	
	private int maxWidth;
	private String suffix;
	
	private ThumbnailSize(int maxWidth, String suffix) {
		this.maxWidth = maxWidth;
		this.suffix = suffix;
	}

	public int getMaxWidth() {
		return maxWidth;
	}

	public String getSuffix() {
		return suffix;
	}
	
	
}
