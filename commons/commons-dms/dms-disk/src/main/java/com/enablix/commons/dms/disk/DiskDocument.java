package com.enablix.commons.dms.disk;

import java.io.InputStream;

import com.enablix.commons.dms.api.Document;
import com.enablix.core.api.ContentLengthAwareDocument;

public class DiskDocument extends Document<DiskDocumentMetadata> implements ContentLengthAwareDocument {

	private InputStream dataStream;
	
	public DiskDocument(InputStream dataStream, String name, String contentType, 
			String contentQId) {
		this(dataStream, new DiskDocumentMetadata(name, contentType, contentQId));
	}
	
	public DiskDocument(InputStream dataStream, DiskDocumentMetadata metadata) {
		this.dataStream = dataStream;
		setMetadata(metadata);
	}
	
	public DiskDocument(DiskDocumentMetadata metadata) {
		setMetadata(metadata);
	}

	@Override
	public InputStream getDataStream() {
		
		if (dataStream == null) {
			dataStream = FileStreamHelper.getFileInputStream(getDocInfo());
		}

		return dataStream;
	}

	@Override
	public long getContentLength() {
		return getMetadata().getContentLength();
	}

}
