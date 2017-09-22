package com.enablix.commons.dms.google.drive;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.dms.api.DocumentBuilder;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.dms.repository.DocumentMetadataRepository;
import com.enablix.commons.util.StringUtil;

@Component
public class GoogleDriveDocumentBuilder implements DocumentBuilder<GoogleDriveDocumentMetadata, GoogleDriveDocument> {

	@Autowired
	private DocumentMetadataRepository metadataRepo;
	
	@Override
	public GoogleDriveDocument build(InputStream dataStream, String name, String contentType, 
			String contentQId, long contentLength, String docIdentity, boolean temporary) {
		
		GoogleDriveDocumentMetadata dbxMd = null;
		
		if (!StringUtil.isEmpty(docIdentity)) {
			DocumentMetadata existMd = metadataRepo.findByIdentity(docIdentity);
		
			if (existMd != null && existMd instanceof GoogleDriveDocumentMetadata) {
				dbxMd = (GoogleDriveDocumentMetadata) existMd;
				if (!dbxMd.getName().equals(name)) {
					dbxMd = null;
				}
			}
		}
		
		GoogleDriveDocument doc = dbxMd != null ? new GoogleDriveDocument(dataStream, dbxMd) 
						: new GoogleDriveDocument(dataStream, name, contentType, contentQId);
		
		doc.getMetadata().setContentLength(contentLength);
		doc.getMetadata().setTemporary(temporary);
		
		return doc;
	}

	@Override
	public GoogleDriveDocument build(InputStream dataStream, String name, String contentType, long contentLength,
			String docIdentity, boolean temporary) {
		return build(dataStream, name, contentType, null, contentLength, docIdentity, temporary);
	}

}
