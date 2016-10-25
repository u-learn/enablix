package com.enablix.app.content.doc;

import java.io.IOException;
import java.io.InputStream;

import com.enablix.commons.dms.api.Document;
import com.enablix.commons.dms.api.DocumentMetadata;

public interface DocumentManager {

	Document<DocumentMetadata> load(String docIdentity) throws IOException;

	DocumentMetadata saveUsingParentInfo(Document<?> doc, String docContainerQId, String docContainerParentInstanceIdentity)
			throws IOException;
	
	DocumentMetadata save(Document<?> doc, String contentPath) throws IOException;

	DocumentMetadata saveUsingContainerInfo(Document<?> doc, String docContainerQId, String docContainerInstanceIdentity)
			throws IOException;
	
	Document<DocumentMetadata> buildDocument(InputStream dataStream, String name, String contentType, 
			String contentQId, long fileSize, String docIdentity, boolean temporaryDoc);

	DocumentMetadata loadDocMetadata(String docIdentity);
	
	DocumentMetadata attachUsingContainerInfo(DocumentMetadata docMd, 
			String docContainerQId, String docContainerInstanceIdentity) throws IOException;
	
	DocumentMetadata attachUsingParentInfo(DocumentMetadata docMd, 
			String docContainerQId, String docContainerParentInstanceIdentity) throws IOException;

	DocumentMetadata delete(DocumentMetadata docMd) throws IOException;

	DocumentMetadata getDocumentMetadata(String docIdentity);
	
}
