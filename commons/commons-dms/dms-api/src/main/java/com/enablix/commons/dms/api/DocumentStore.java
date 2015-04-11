package com.enablix.commons.dms.api;

import java.io.IOException;


public interface DocumentStore<DM extends DocumentMetadata, D extends Document<DM>> {

	DM save(D document) throws IOException;
	
	D load(DM docMetadata);
	
	boolean canHandle(DocumentMetadata docMetadata);
	
	boolean canHandle(Document<?> doc);
	
}
