package com.enablix.app.content.doc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.enablix.commons.dms.api.Document;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.dms.api.DocumentStore;
import com.enablix.commons.util.beans.SpringBackedAbstractFactory;

@SuppressWarnings("rawtypes")
@Component
public class DocumentStoreFactoryImpl extends SpringBackedAbstractFactory<DocumentStore> implements DocumentStoreFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentStoreFactoryImpl.class);
	
	@Override
	public DocumentStore getDocumentStore(DocumentMetadata dm) {
		
		for (DocumentStore ds : registeredInstances()) {
			if (ds.canHandle(dm)) {
				return ds;
			}
		}

		LOGGER.error("No document store found for [{}] document metadata", 
						dm.getClass().getSimpleName());
		
		throw new IllegalStateException("No document store found for [" 
				+ dm.getClass().getSimpleName() + "] document metadata");
	}

	@SuppressWarnings("unchecked")
	@Override
	public DocumentStore getDocumentStore(Document doc) {
		
		for (DocumentStore ds : registeredInstances()) {
			if (ds.canHandle(doc)) {
				return ds;
			}
		}

		LOGGER.error("No document store found for [{}] document", 
				doc.getClass().getSimpleName());
		
		throw new IllegalStateException("No document store found for [" 
						+ doc.getClass().getSimpleName() + "] document");
	}

	@Override
	protected Class<DocumentStore> lookupForType() {
		return DocumentStore.class;
	}

}
