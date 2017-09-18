package com.enablix.app.content.update;

import java.util.Map;

import com.enablix.core.domain.content.quality.QualityAnalysis;

public class UpdateContentResponse {

	private Map<String, Object> contentRecord;
	
	private QualityAnalysis qualityAnalysis;

	public UpdateContentResponse() {
	}

	public UpdateContentResponse(Map<String, Object> contentRecord, QualityAnalysis qualityAnalysis) {
		super();
		this.contentRecord = contentRecord;
		this.qualityAnalysis = qualityAnalysis;
	}


	public Map<String, Object> getContentRecord() {
		return contentRecord;
	}

	public void setContentRecord(Map<String, Object> savedRecord) {
		this.contentRecord = savedRecord;
	}

	public QualityAnalysis getQualityAnalysis() {
		return qualityAnalysis;
	}

	public void setQualityAnalysis(QualityAnalysis qualityAnalysis) {
		this.qualityAnalysis = qualityAnalysis;
	}
	
}
