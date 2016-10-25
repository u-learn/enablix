package com.enablix.commons.dms.dropbox;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.dms.api.DocumentBuilder;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.dms.repository.DocumentMetadataRepository;
import com.enablix.commons.util.StringUtil;

@Component
public class DropboxDocumentBuilder implements DocumentBuilder<DropboxDocumentMetadata, DropboxDocument> {

	@Autowired
	private DocumentMetadataRepository metadataRepo;
	
	@Override
	public DropboxDocument build(InputStream dataStream, String name, String contentType, 
			String contentQId, long contentLength, String docIdentity, boolean temporary) {
		
		DropboxDocumentMetadata dbxMd = null;
		
		if (!StringUtil.isEmpty(docIdentity)) {
			DocumentMetadata existMd = metadataRepo.findByIdentity(docIdentity);
		
			if (existMd != null && existMd instanceof DropboxDocumentMetadata) {
				dbxMd = (DropboxDocumentMetadata) existMd;
				if (!dbxMd.getName().equals(name)) {
					dbxMd = null;
				}
			}
		}
		
		DropboxDocument doc = dbxMd != null ? new DropboxDocument(dataStream, dbxMd) 
						: new DropboxDocument(dataStream, name, contentType, contentQId);
		
		doc.getMetadata().setContentLength(contentLength);
		doc.getMetadata().setTemporary(temporary);
		
		return doc;
	}

	@Override
	public DropboxDocument build(InputStream dataStream, String name, String contentType, long contentLength,
			String docIdentity, boolean temporary) {
		return build(dataStream, name, contentType, null, contentLength, docIdentity, temporary);
	}

}
