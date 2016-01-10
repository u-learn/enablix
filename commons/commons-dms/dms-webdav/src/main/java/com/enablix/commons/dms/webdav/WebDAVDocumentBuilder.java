package com.enablix.commons.dms.webdav;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.dms.api.DocumentBuilder;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.dms.repository.DocumentMetadataRepository;

@Component
public class WebDAVDocumentBuilder implements DocumentBuilder<WebDAVDocumentMetadata, WebDAVDocument> {

	@Autowired
	private DocumentMetadataRepository metadataRepo;
	
	@Override
	public WebDAVDocument build(InputStream dataStream, String name, String contentType, String contentQId,
			long contentLength, String docIdentity) {
		
		DocumentMetadata existMd = metadataRepo.findByIdentity(docIdentity);
		WebDAVDocumentMetadata dbxMd = null;
		
		if (existMd != null && existMd instanceof WebDAVDocumentMetadata) {
			dbxMd = (WebDAVDocumentMetadata) existMd;
			if (!dbxMd.getName().equals(name)) {
				dbxMd = null;
			}
		}
		
		WebDAVDocument doc = dbxMd != null ? new WebDAVDocument(dataStream, dbxMd) 
						: new WebDAVDocument(dataStream, name, contentType, contentQId);
		
		doc.getMetadata().setContentLength(contentLength);
		
		return doc;
	}

}
