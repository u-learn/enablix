package com.enablix.core.domain.content.quality;

import java.util.Collection;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.domain.BaseDocumentEntity;

@Document(collection = "ebx_content_quality_analysis")
public class ContentQualityAnalysis extends BaseDocumentEntity {

	private String contentIdentity;
	
	private String contentQId;
	
	private QualityAnalysis analysis;

	public ContentQualityAnalysis(String contentIdentity, String contentQId) {
		super();
		this.contentIdentity = contentIdentity;
		this.contentQId = contentQId;
		this.analysis = new QualityAnalysis();
	}

	public String getContentIdentity() {
		return contentIdentity;
	}

	public void setContentIdentity(String contentIdentity) {
		this.contentIdentity = contentIdentity;
	}

	public String getContentQId() {
		return contentQId;
	}

	public void setContentQId(String contentQId) {
		this.contentQId = contentQId;
	}
	
	public void addAlert(QualityAlert alert) {
		analysis.addAlert(alert);
	}
	
	public void addAlerts(Collection<QualityAlert> alerts) {
		analysis.addAlerts(alerts);
	}

	public QualityAnalysis getAnalysis() {
		return analysis;
	}

	public void setAnalysis(QualityAnalysis analysis) {
		this.analysis = analysis;
	}
	
}
