package com.enablix.core.ui;

public class TextPreviewInfo extends ContentPreviewInfo {

	private String text;
	
	public TextPreviewInfo() {
		super(PreviewType.TEXT);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
