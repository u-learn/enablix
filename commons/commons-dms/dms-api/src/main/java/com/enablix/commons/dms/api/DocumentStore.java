package com.enablix.commons.dms.api;

import java.io.IOException;

import com.enablix.core.api.DocInfo;
import com.enablix.core.api.IDocument;


public interface DocumentStore<DM extends DocumentMetadata, D extends Document<DM>> {

	DM save(D document, String contentPath) throws IOException;
	
	DocInfo save(IDocument document, String path) throws IOException;
	
	D load(DM docMetadata) throws IOException;
	
	IDocument load(DocInfo docInfo) throws IOException;
	
	DM move(DM docMetadata, String newContentPath) throws IOException;
	
	void delete(DM docMetadata) throws IOException;
	
	boolean canHandle(DocumentMetadata docMetadata);
	
	boolean canHandle(Document<?> doc);
	
	String type();
	
	DocumentBuilder<DM, D> getDocumentBuilder();
	
}
