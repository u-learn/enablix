package com.enablix.dms.onedrive;

import java.io.InputStream;

import com.enablix.commons.dms.api.Document;
import com.enablix.core.api.ContentLengthAwareDocument;

public class OneDriveDocument extends Document<OneDriveDocumentMetadata> implements ContentLengthAwareDocument {

	private InputStream dataStream;
	
	public OneDriveDocument(InputStream dataStream, String name, String contentType, 
			String contentQId) {
		this(dataStream, new OneDriveDocumentMetadata(name, contentType, contentQId));
	}
	
	public OneDriveDocument(InputStream dataStream, OneDriveDocumentMetadata metadata) {
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
