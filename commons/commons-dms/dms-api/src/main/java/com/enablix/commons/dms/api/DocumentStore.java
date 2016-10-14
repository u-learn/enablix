package com.enablix.commons.dms.api;

import java.io.IOException;


public interface DocumentStore<DM extends DocumentMetadata, D extends Document<DM>> {

	DM save(D document, String contentPath) throws IOException;
	
	D load(DM docMetadata) throws IOException;
	
	DM move(DM docMetadata, String newContentPath) throws IOException;
	
	void delete(DM docMetadata) throws IOException;
	
	boolean canHandle(DocumentMetadata docMetadata);
	
	boolean canHandle(Document<?> doc);
	
	String type();
	
	DocumentBuilder<DM, D> getDocumentBuilder();
	
}
