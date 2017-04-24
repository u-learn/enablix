package com.enablix.analytics.info.detection;

import org.springframework.util.Assert;

public abstract class Opinion {

	private String opinionBy;
	
	private float confidence; // between 0-1

	protected Opinion() { 
		// for ORM
	}
	
	protected Opinion(String opinionBy, float confidence) {
		
		super();
		
		this.opinionBy = opinionBy;
		this.confidence = confidence;
		
		Assert.isTrue(0 <= confidence && confidence <= 1, "Confidence should be range [0-1]");
	}

	public String getOpinionBy() {
		return opinionBy;
	}

	public void setOpinionBy(String opinionBy) {
		this.opinionBy = opinionBy;
	}

	public float getConfidence() {
		return confidence;
	}

	public void setConfidence(float confidence) {
		this.confidence = confidence;
	}
	
}
