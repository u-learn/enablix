package com.enablix.core.ui;

public class Hyperlink {

	private String href;
	
	private String title;
	
	private String rawText;

	public Hyperlink(String href, String title, String rawText) {
		super();
		this.href = href;
		this.title = title;
		this.rawText = rawText;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRawText() {
		return rawText;
	}

	public void setRawText(String rawText) {
		this.rawText = rawText;
	}
	
	
	
}
