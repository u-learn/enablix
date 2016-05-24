package com.enablix.core.correlation;

public class CorrelationSource<SM extends SourceMetadata> {

	private float score;
	
	private SM metadata;

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public SM getMetadata() {
		return metadata;
	}

	public void setMetadata(SM metadata) {
		this.metadata = metadata;
	}
	
}
