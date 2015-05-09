package com.enablix.commons.dms.api;

import java.io.InputStream;


public abstract class Document<DM extends DocumentMetadata> {

	private DM metadata;

	public DM getMetadata() {
		return metadata;
	}

	public void setMetadata(DM metadata) {
		this.metadata = metadata;
	}
	
	public abstract InputStream getDataStream();
	
}
