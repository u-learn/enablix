package com.enablix.content.connection.web;

import java.util.ArrayList;
import java.util.List;

public class ContentTypeLinkVO {

	private String contentQId;
	
	private List<ContentValueLinkVO> valueLinks;

	public ContentTypeLinkVO() {
		super();
		this.valueLinks = new ArrayList<>();
	}

	public String getContentQId() {
		return contentQId;
	}

	public void setContentQId(String contentQId) {
		this.contentQId = contentQId;
	}

	public List<ContentValueLinkVO> getValueLinks() {
		return valueLinks;
	}

	public void setValueLinks(List<ContentValueLinkVO> valueLinks) {
		this.valueLinks = valueLinks;
	}
	
	public void addValueLink(ContentValueLinkVO valueLink) {
		valueLinks.add(valueLink);
	}
	
}
