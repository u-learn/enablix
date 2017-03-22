package com.enablix.dms.sharepoint;

import java.io.IOException;
import java.io.InputStream;

import com.enablix.commons.dms.api.Document;

public class SharepointDocument extends Document<SharepointDocumentMetadata> {

	private InputStream dataStream;
	
	public SharepointDocument(InputStream dataStream, String name, String contentType, String contentQId) {
		this(dataStream, new SharepointDocumentMetadata(name, contentType, contentQId));
	}
	
	public SharepointDocument(InputStream dataStream, SharepointDocumentMetadata metadata) {
		this.dataStream = dataStream;
		setMetadata(metadata);
	}
	
	@Override
	public InputStream getDataStream() {
		return dataStream;
	}

	@Override
	protected void finalize() {
		// clean up
		try {
			dataStream.close();
		} catch (IOException e) {
			// ignore
		}
	}
	
}
