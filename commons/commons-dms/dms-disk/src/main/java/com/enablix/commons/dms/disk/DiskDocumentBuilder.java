package com.enablix.commons.dms.disk;

import java.io.InputStream;

import com.enablix.commons.dms.api.DocumentBuilder;

public class DiskDocumentBuilder implements DocumentBuilder<DiskDocumentMetadata, DiskDocument> {

	@Override
	public DiskDocument build(InputStream dataStream, String name, 
			String contentType, String contentQId, long contentLength, String docIdentity) {
		DiskDocument doc = new DiskDocument(dataStream, name, contentType, contentQId);
		doc.getMetadata().setContentLength(contentLength);
		return doc;
	}

}
