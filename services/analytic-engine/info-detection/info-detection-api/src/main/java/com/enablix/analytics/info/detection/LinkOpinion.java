package com.enablix.analytics.info.detection;

import com.enablix.core.api.ContentDataRef;

public class LinkOpinion extends Opinion {

	private ContentDataRef linkedRecord;
	
	protected LinkOpinion() {
		// for ORM
	}
	
	public LinkOpinion(ContentDataRef linkedRecord, String opinionBy, float confidence) {
		super(opinionBy, confidence);
		this.linkedRecord = linkedRecord;
	}

	public ContentDataRef getLinkedRecord() {
		return linkedRecord;
	}

	@Override
	public String toString() {
		return "LinkOpinion [linkedRecord=" + linkedRecord + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((linkedRecord == null) ? 0 : linkedRecord.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LinkOpinion other = (LinkOpinion) obj;
		if (linkedRecord == null) {
			if (other.linkedRecord != null)
				return false;
		} else if (!linkedRecord.equals(other.linkedRecord))
			return false;
		return true;
	}
	
	
	
}
