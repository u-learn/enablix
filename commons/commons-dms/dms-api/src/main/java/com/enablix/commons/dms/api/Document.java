package com.enablix.commons.dms.api;


public abstract class Document<DM extends DocumentMetadata> {

	private DM metadata;

	public DM getMetadata() {
		return metadata;
	}

	public void setMetadata(DM metadata) {
		this.metadata = metadata;
	}
	
}
