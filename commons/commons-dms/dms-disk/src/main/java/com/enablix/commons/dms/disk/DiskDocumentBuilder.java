package com.enablix.commons.dms.disk;

import java.io.InputStream;

import com.enablix.commons.dms.api.DocumentBuilder;

public class DiskDocumentBuilder implements DocumentBuilder<DiskDocumentMetadata, DiskDocument> {

	@Override
	public DiskDocument build(InputStream dataStream, String name, String contentType, 
			String contentQId, long contentLength, String docIdentity, boolean temporary) {
		DiskDocument doc = new DiskDocument(dataStream, name, contentType, contentQId);
		doc.getMetadata().setContentLength(contentLength);
		doc.getMetadata().setTemporary(temporary);
		return doc;
	}

	@Override
	public DiskDocument build(InputStream dataStream, String name, String contentType, long contentLength,
			String docIdentity, boolean temporary) {
		return build(dataStream, name, contentType, null, contentLength, docIdentity, temporary);
	}

}
