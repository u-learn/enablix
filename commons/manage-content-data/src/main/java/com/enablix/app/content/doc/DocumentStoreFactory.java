package com.enablix.app.content.doc;

import com.enablix.commons.dms.api.Document;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.dms.api.DocumentStore;

public interface DocumentStoreFactory {

	@SuppressWarnings("rawtypes")
	DocumentStore getDocumentStore(DocumentMetadata dm);
	
	@SuppressWarnings("rawtypes")
	DocumentStore getDocumentStore(Document doc);

	@SuppressWarnings("rawtypes")
	DocumentStore getDocumentStore(String storeType);
	
	String defaultStoreType();
}
