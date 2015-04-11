package com.enablix.commons.dms.disk;

import java.io.InputStream;

import com.enablix.commons.dms.api.Document;

public class DiskDocument extends Document<DiskDocumentMetadata> {

	private InputStream dataStream;
	
	public DiskDocument(InputStream dataStream, String name) {
		this(dataStream, new DiskDocumentMetadata(name));
	}
	
	public DiskDocument(InputStream dataStream, DiskDocumentMetadata metadata) {
		this.dataStream = dataStream;
		setMetadata(metadata);
	}

	public InputStream getDataStream() {
		return dataStream;
	}

}
