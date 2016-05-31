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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((metadata == null) ? 0 : metadata.hashCode());
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CorrelationSource other = (CorrelationSource) obj;
		if (metadata == null) {
			if (other.metadata != null)
				return false;
		} else if (!metadata.equals(other.metadata))
			return false;
		return true;
	}
	
}
