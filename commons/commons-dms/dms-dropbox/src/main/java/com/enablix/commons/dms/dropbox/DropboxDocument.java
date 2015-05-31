package com.enablix.commons.dms.dropbox;

import java.io.InputStream;

import com.enablix.commons.dms.api.ContentLengthAwareDocument;
import com.enablix.commons.dms.api.Document;

public class DropboxDocument extends Document<DropboxDocumentMetadata> implements ContentLengthAwareDocument {
	
	private InputStream dataStream;
	
	public DropboxDocument(InputStream dataStream, String name, String contentType, 
			String contentQId) {
		this(dataStream, new DropboxDocumentMetadata(name, contentType, contentQId));
	}
	
	public DropboxDocument(InputStream dataStream, DropboxDocumentMetadata metadata) {
		this.dataStream = dataStream;
		setMetadata(metadata);
	}
	
	@Override
	public InputStream getDataStream() {
		return dataStream;
	}

	@Override
	public long getContentLength() {
		return getMetadata().getContentLength();
	}

}
