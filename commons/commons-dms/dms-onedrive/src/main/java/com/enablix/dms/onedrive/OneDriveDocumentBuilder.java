package com.enablix.dms.onedrive;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.dms.api.DocumentBuilder;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.dms.repository.DocumentMetadataRepository;
import com.enablix.commons.util.StringUtil;

@Component
public class OneDriveDocumentBuilder  implements DocumentBuilder<OneDriveDocumentMetadata, OneDriveDocument> {

	@Autowired
	private DocumentMetadataRepository metadataRepo;
	
	@Override
	public OneDriveDocument build(InputStream dataStream, String name, String contentType, 
			String contentQId, long contentLength, String docIdentity, boolean temporary) {
		
		OneDriveDocumentMetadata dbxMd = null;
		
		if (!StringUtil.isEmpty(docIdentity)) {
			DocumentMetadata existMd = metadataRepo.findByIdentity(docIdentity);
		
			if (existMd != null && existMd instanceof OneDriveDocumentMetadata) {
				dbxMd = (OneDriveDocumentMetadata) existMd;
				if (!dbxMd.getName().equals(name)) {
					dbxMd = null;
				}
			}
		}
		
		OneDriveDocument doc = dbxMd != null ? new OneDriveDocument(dataStream, dbxMd) 
						: new OneDriveDocument(dataStream, name, contentType, contentQId);
		
		doc.getMetadata().setContentLength(contentLength);
		doc.getMetadata().setTemporary(temporary);
		
		return doc;
	}

	@Override
	public OneDriveDocument build(InputStream dataStream, String name, String contentType, long contentLength,
			String docIdentity, boolean temporary) {
		return build(dataStream, name, contentType, null, contentLength, docIdentity, temporary);
	}
	
}
