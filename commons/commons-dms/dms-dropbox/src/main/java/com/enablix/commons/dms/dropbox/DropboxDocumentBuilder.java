package com.enablix.commons.dms.dropbox;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.dms.api.DocumentBuilder;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.dms.repository.DocumentMetadataRepository;

@Component
public class DropboxDocumentBuilder implements DocumentBuilder<DropboxDocumentMetadata, DropboxDocument> {

	@Autowired
	private DocumentMetadataRepository metadataRepo;
	
	@Override
	public DropboxDocument build(InputStream dataStream, String name, 
			String contentType, String contentQId, long contentLength, String docIdentity) {
		
		DocumentMetadata existMd = metadataRepo.findByIdentity(docIdentity);
		DropboxDocumentMetadata dbxMd = null;
		
		if (existMd != null && existMd instanceof DropboxDocumentMetadata) {
			dbxMd = (DropboxDocumentMetadata) existMd;
			if (!dbxMd.getName().equals(name)) {
				dbxMd = null;
			}
		}
		
		DropboxDocument doc = dbxMd != null ? new DropboxDocument(dataStream, dbxMd) 
						: new DropboxDocument(dataStream, name, contentType, contentQId);
		
		doc.getMetadata().setContentLength(contentLength);
		
		return doc;
	}

}
