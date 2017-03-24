package com.enablix.dms.sharepoint;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.dms.api.DocumentBuilder;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.dms.repository.DocumentMetadataRepository;
import com.enablix.commons.util.StringUtil;

@Component
public class SharepointDocumentBuilder implements DocumentBuilder<SharepointDocumentMetadata, SharepointDocument> {

	@Autowired
	private DocumentMetadataRepository metadataRepo;
	
	@Override
	public SharepointDocument build(InputStream dataStream, String name, String contentType, String contentQId,
			long contentLength, String docIdentity, boolean temporary) {
		
		SharepointDocumentMetadata dbxMd = null;
		
		if (!StringUtil.isEmpty(docIdentity)) {

			DocumentMetadata existMd = metadataRepo.findByIdentity(docIdentity);
			
			if (existMd != null && existMd instanceof SharepointDocumentMetadata) {
				dbxMd = (SharepointDocumentMetadata) existMd;
				if (!dbxMd.getName().equals(name)) {
					dbxMd = null;
				}
			}
		}
		
		SharepointDocument doc = dbxMd != null ? new SharepointDocument(dataStream, dbxMd) 
						: new SharepointDocument(dataStream, name, contentType, contentQId);
		
		doc.getMetadata().setContentLength(contentLength);
		doc.getMetadata().setTemporary(temporary);
		
		return doc;
	}

	@Override
	public SharepointDocument build(InputStream dataStream, String name, String contentType, long contentLength,
			String docIdentity, boolean temporary) {
		return build(dataStream, name, contentType, null, contentLength, docIdentity, temporary);
	}
}