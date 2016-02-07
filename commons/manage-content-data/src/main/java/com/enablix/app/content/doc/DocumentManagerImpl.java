package com.enablix.app.content.doc;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.ContentDataPathResolver;
import com.enablix.commons.config.ConfigurationUtil;
import com.enablix.commons.dms.DocumentStoreConstants;
import com.enablix.commons.dms.api.Document;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.dms.api.DocumentStore;
import com.enablix.commons.dms.repository.DocumentMetadataRepository;
import com.enablix.commons.util.QIdUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.domain.config.Configuration;

@Component
public class DocumentManagerImpl implements DocumentManager {

	@Autowired
	private DocumentStoreFactory storeFactory;
	
	@Autowired
	private DocumentMetadataRepository docRepo;
	
	@Autowired
	private ContentDataPathResolver pathResolver;
	
	@Override
	public DocumentMetadata saveUsingParentInfo(Document<?> doc, String docContainerQId, 
			String docContainerParentInstanceIdentity) throws IOException {
		
		String contentDataPath = pathResolver.resolveContentDataPath(
				ProcessContext.get().getTemplateId(), 
				QIdUtil.getParentQId(docContainerQId), 
				docContainerParentInstanceIdentity);
		
		contentDataPath = pathResolver.addContainerLabelToPath(
				ProcessContext.get().getTemplateId(), 
				docContainerQId, contentDataPath);
		
		return save(doc, contentDataPath);
	}

	@Override
	public DocumentMetadata saveUsingContainerInfo(Document<?> doc, String docContainerQId, 
			String docContainerInstanceIdentity) throws IOException {
		
		String contentDataPath = pathResolver.resolveContentParentDataPath(
				ProcessContext.get().getTemplateId(), 
				docContainerQId, docContainerInstanceIdentity);
		
		contentDataPath = pathResolver.addContainerLabelToPath(
				ProcessContext.get().getTemplateId(), 
				docContainerQId, contentDataPath);
		
		return save(doc, contentDataPath);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Document<DocumentMetadata> load(String docIdentity) throws IOException {
		
		DocumentMetadata docMD = docRepo.findByIdentity(docIdentity);
		
		if (docMD == null) {
			throw new IllegalArgumentException("document [" + docIdentity + "] not found");
		}
		
		DocumentStore documentStore = storeFactory.getDocumentStore(docMD);
		Document<DocumentMetadata> doc = documentStore.load(docMD);
		
		return doc;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public DocumentMetadata save(Document<?> doc, String contentPath) throws IOException {
		
		DocumentStore ds = storeFactory.getDocumentStore(doc);
		
		DocumentMetadata docMD = ds.save(doc, contentPath);
		
		docMD = docRepo.save(docMD);
		
		return docMD;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Document<DocumentMetadata> buildDocument(InputStream dataStream, 
			String name, String contentType, String contentQId, long contentLength, String docIdentity) {
		
		Configuration docStoreConfig = ConfigurationUtil.getConfig(DocumentStoreConstants.DEFUALT_DOC_STORE_CONFIG_KEY);
		
		String storeType = null;
		if (docStoreConfig != null) {
			storeType = docStoreConfig.getStringValue(DocumentStoreConstants.DOC_STORE_TYPE_PROP);
		} else {
			storeType = storeFactory.defaultStoreType();
		}
		
		DocumentStore ds = storeFactory.getDocumentStore(storeType);
		return ds.getDocumentBuilder().build(dataStream, name, contentType, contentQId, contentLength, docIdentity);
	}

}
