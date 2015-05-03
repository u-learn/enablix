package com.enablix.app.content.doc;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.dms.api.Document;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.dms.api.DocumentStore;

@Component
public class DocumentManagerImpl implements DocumentManager {

	@Autowired
	private DocumentStoreFactory storeFactory;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public DocumentMetadata save(Document<?> doc) throws IOException {
		
		DocumentStore ds = storeFactory.getDocumentStore(doc);
		ds.save(doc);
		
		return doc.getMetadata();
	}

}
