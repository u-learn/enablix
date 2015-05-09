package com.enablix.app.content.doc;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.dms.api.Document;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.dms.api.DocumentStore;
import com.enablix.commons.dms.repository.DocumentMetadataRepository;

@Component
public class DocumentManagerImpl implements DocumentManager {

	@Autowired
	private DocumentStoreFactory storeFactory;
	
	@Autowired
	private DocumentMetadataRepository docRepo;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public DocumentMetadata save(Document<?> doc) throws IOException {
		
		DocumentStore ds = storeFactory.getDocumentStore(doc);
		DocumentMetadata docMD = ds.save(doc);
		
		docMD = docRepo.save(docMD);
		
		return docMD;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Document<DocumentMetadata> load(String docIdentity) {
		
		DocumentMetadata docMD = docRepo.findByIdentity(docIdentity);
		
		if (docMD == null) {
			throw new IllegalArgumentException("document [" + docIdentity + "] not found");
		}
		
		DocumentStore documentStore = storeFactory.getDocumentStore(docMD);
		Document<DocumentMetadata> doc = documentStore.load(docMD);
		
		return doc;
	}

}
