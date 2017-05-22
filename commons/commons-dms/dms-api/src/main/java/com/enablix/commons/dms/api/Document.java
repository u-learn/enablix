package com.enablix.commons.dms.api;

import java.io.InputStream;

import com.enablix.core.api.IDocument;

public abstract class Document<DM extends DocumentMetadata> implements IDocument {

	private DM metadata;

	public DM getMetadata() {
		return metadata;
	}

	public void setMetadata(DM metadata) {
		this.metadata = metadata;
	}
	
	@Override
	public DM getDocInfo() {
		return getMetadata();
	}
	
	public abstract InputStream getDataStream();
	
}
