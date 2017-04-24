package com.enablix.analytics.info.detection;

import com.enablix.core.api.ContentRecordRef;

public class LinkOpinion extends Opinion {

	private ContentRecordRef linkedRecord;
	
	protected LinkOpinion() {
		// for ORM
	}
	
	public LinkOpinion(ContentRecordRef linkedRecord, String opinionBy, float confidence) {
		super(opinionBy, confidence);
		this.linkedRecord = linkedRecord;
	}

	public ContentRecordRef getLinkedRecord() {
		return linkedRecord;
	}
	
}
