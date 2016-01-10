package com.enablix.commons.dms.webdav;

import java.io.InputStream;

import com.enablix.commons.dms.api.Document;

public class WebDAVDocument extends Document<WebDAVDocumentMetadata> {

	private InputStream dataStream;
	
	public WebDAVDocument(InputStream dataStream, String name, String contentType, 
			String contentQId) {
		this(dataStream, new WebDAVDocumentMetadata(name, contentType, contentQId));
	}
	
	public WebDAVDocument(InputStream dataStream, WebDAVDocumentMetadata metadata) {
		this.dataStream = dataStream;
		setMetadata(metadata);
	}
	
	@Override
	public InputStream getDataStream() {
		return dataStream;
	}

}
