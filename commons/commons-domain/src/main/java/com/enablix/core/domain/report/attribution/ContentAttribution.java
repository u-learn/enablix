package com.enablix.core.domain.report.attribution;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.domain.BaseDocumentEntity;

@Document(collection = "ebx_content_attribution_demo")
public class ContentAttribution extends BaseDocumentEntity {

	private String contentTitle;
	
	private String contentQId;
	
	private Attribution oppAttribution;

	public String getContentTitle() {
		return contentTitle;
	}

	public void setContentTitle(String contentTitle) {
		this.contentTitle = contentTitle;
	}

	public String getContentQId() {
		return contentQId;
	}

	public void setContentQId(String contentQId) {
		this.contentQId = contentQId;
	}

	public Attribution getOppAttribution() {
		return oppAttribution;
	}

	public void setOppAttribution(Attribution oppAttribution) {
		this.oppAttribution = oppAttribution;
	}
	
}
