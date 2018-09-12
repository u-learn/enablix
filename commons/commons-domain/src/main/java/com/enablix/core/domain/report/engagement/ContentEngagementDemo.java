package com.enablix.core.domain.report.engagement;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.domain.BaseDocumentEntity;

@Document(collection = "ebx_content_engagement_demo")
public class ContentEngagementDemo extends BaseDocumentEntity {

	private String contentTitle;
	
	private String contentQId;
	
	private EngagementMetric internalAccess;
	
	private EngagementMetric internalDownloads;
	
	private EngagementMetric externalDownloads;
	
	private EngagementMetric internalShares;
	
	private EngagementMetric externalShares;

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

	public EngagementMetric getInternalAccess() {
		return internalAccess;
	}

	public void setInternalAccess(EngagementMetric internalAccess) {
		this.internalAccess = internalAccess;
	}

	public EngagementMetric getInternalDownloads() {
		return internalDownloads;
	}

	public void setInternalDownloads(EngagementMetric internalDownloads) {
		this.internalDownloads = internalDownloads;
	}

	public EngagementMetric getExternalDownloads() {
		return externalDownloads;
	}

	public void setExternalDownloads(EngagementMetric externalDownloads) {
		this.externalDownloads = externalDownloads;
	}

	public EngagementMetric getInternalShares() {
		return internalShares;
	}

	public void setInternalShares(EngagementMetric internalShares) {
		this.internalShares = internalShares;
	}

	public EngagementMetric getExternalShares() {
		return externalShares;
	}

	public void setExternalShares(EngagementMetric externalShares) {
		this.externalShares = externalShares;
	}
	
}
