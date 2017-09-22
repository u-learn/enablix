package com.enablix.commons.dms.google.drive;

import java.io.InputStream;

import com.enablix.commons.dms.api.Document;
import com.enablix.commons.util.IOUtil;
import com.enablix.core.api.ContentLengthAwareDocument;

public class GoogleDriveDocument extends Document<GoogleDriveDocumentMetadata> implements ContentLengthAwareDocument {
	
	private InputStream dataStream;
	
	public GoogleDriveDocument(InputStream dataStream, String name, String contentType, 
			String contentQId) {
		this(dataStream, new GoogleDriveDocumentMetadata(name, contentType, contentQId));
	}
	
	public GoogleDriveDocument(InputStream dataStream, GoogleDriveDocumentMetadata metadata) {
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
	
	public void finalize() {
		IOUtil.closeStream(dataStream);
	}

}
