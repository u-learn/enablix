package com.enablix.commons.dms.disk;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.dms.api.DocumentBuilder;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.dms.repository.DocumentMetadataRepository;
import com.enablix.commons.util.StringUtil;

@Component
public class DiskDocumentBuilder implements DocumentBuilder<DiskDocumentMetadata, DiskDocument> {

	@Autowired
	private DocumentMetadataRepository metadataRepo;
	
	@Override
	public DiskDocument build(InputStream dataStream, String name, String contentType, 
			String contentQId, long contentLength, String docIdentity, boolean temporary) {
		
		DiskDocumentMetadata dbxMd = null;
		
		if (!StringUtil.isEmpty(docIdentity)) {
			DocumentMetadata existMd = metadataRepo.findByIdentity(docIdentity);
		
			if (existMd != null && existMd instanceof DiskDocumentMetadata) {
				dbxMd = (DiskDocumentMetadata) existMd;
			}
		}
		
		DiskDocument doc = new DiskDocument(dataStream, name, contentType, contentQId);
		
		if (dbxMd != null) {
			doc.getMetadata().setId(dbxMd.getId());
			doc.getMetadata().setIdentity(dbxMd.getIdentity());
		}
		
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
