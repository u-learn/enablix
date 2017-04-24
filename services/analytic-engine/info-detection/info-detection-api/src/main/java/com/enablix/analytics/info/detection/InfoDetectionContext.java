package com.enablix.analytics.info.detection;

public class InfoDetectionContext {

	private Assessment assessment;

	public InfoDetectionContext(Assessment assessment) {
		this.assessment = assessment;
	}
	
	public Assessment getAssessment() {
		return assessment;
	}
	
}
