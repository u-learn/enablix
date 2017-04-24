package com.enablix.analytics.info.detection;

public class TypeOpinion extends Opinion {

	private String containerQId; // opinion type
	
	protected TypeOpinion() {
		// for ORM
	}
	
	public TypeOpinion(String containerQId, String opinionBy, float confidence) {
		super(opinionBy, confidence);
		this.containerQId = containerQId;
	}

	public String getContainerQId() {
		return containerQId;
	}
	
}
