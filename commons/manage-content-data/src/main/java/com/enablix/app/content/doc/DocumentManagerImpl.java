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
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.domain.config.Configuration;

@Component
public class DocumentManagerImpl implements DocumentManager {

	private static final String TEMP_SUB_FOLDER = "tmp";
	
	@Autowired
	private DocumentStoreFactory storeFactory;
	
	@Autowired
	private DocumentMetadataRepository docRepo;
	
	@Autowired
	private ContentDataPathResolver pathResolver;
	
	@Override
	public DocumentMetadata saveUsingParentInfo(Document<?> doc, String docContainerQId, 
			String docContainerParentInstanceIdentity) throws IOException {
		
		String contentPath = createContentPathUsingParentInfo(docContainerQId, 
				docContainerParentInstanceIdentity, doc.getMetadata().isTemporary());
		return save(doc, contentPath);
	}

	private String createContentPathUsingParentInfo(String docContainerQId, 
			String docContainerParentInstanceIdentity, boolean temporaryDoc) {
		
		String templateId = ProcessContext.get().getTemplateId();
		String contentDataPath = pathResolver.resolveContainerPath(templateId, docContainerQId);
		
		if (temporaryDoc) {
			contentDataPath = pathResolver.appendPath(contentDataPath, TEMP_SUB_FOLDER);
		}
		
		return contentDataPath;
	}

	@Override
	public DocumentMetadata saveUsingContainerInfo(Document<?> doc, String docContainerQId, 
			String docContainerInstanceIdentity) throws IOException {
		
		String contentPath = createContentPathUsingContainerInfo(docContainerQId, 
				docContainerInstanceIdentity, doc.getMetadata().isTemporary());
		return save(doc, contentPath);
	}

	private String createContentPathUsingContainerInfo(String docContainerQId, 
			String docContainerInstanceIdentity, boolean temporaryDoc) {
		
		String templateId = ProcessContext.get().getTemplateId();
		String contentDataPath = pathResolver.resolveContainerPath(templateId, docContainerQId);
		
		if (temporaryDoc) {
			contentDataPath = pathResolver.appendPath(contentDataPath, TEMP_SUB_FOLDER);
		}
		
		return contentDataPath;
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
	

	@Override
	public DocumentMetadata loadDocMetadata(String docIdentity) {
		return docRepo.findByIdentity(docIdentity);
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
	public Document<DocumentMetadata> buildDocument(InputStream dataStream, String name, 
			String contentType, String contentQId, long contentLength, String docIdentity, boolean tmpDoc) {
		
		Configuration docStoreConfig = ConfigurationUtil.getConfig(DocumentStoreConstants.DEFUALT_DOC_STORE_CONFIG_KEY);
		
		String storeType = null;
		if (docStoreConfig != null) {
			storeType = docStoreConfig.getStringValue(DocumentStoreConstants.DEFUALT_DOC_STORE_CONFIG_PROP);
		} else {
			storeType = storeFactory.defaultStoreType();
		}
		
		DocumentStore ds = storeFactory.getDocumentStore(storeType);
		return ds.getDocumentBuilder().build(dataStream, name, contentType, contentQId, contentLength, docIdentity, tmpDoc);
	}

	@Override
	public DocumentMetadata attachUsingContainerInfo(DocumentMetadata docMd, String docContainerQId,
			String docContainerInstanceIdentity) throws IOException {
		
		String contentPath = createContentPathUsingContainerInfo(docContainerQId, 
				docContainerInstanceIdentity, false);
		
		docMd.setTemporary(false);
		
		return move(docMd, contentPath);
	}

	@Override
	public DocumentMetadata attachUsingParentInfo(DocumentMetadata docMd, String docContainerQId,
			String docContainerParentInstanceIdentity) throws IOException {
		
		String contentPath = createContentPathUsingParentInfo(docContainerQId, 
				docContainerParentInstanceIdentity, false);
		
		docMd.setTemporary(false);
		
		return move(docMd, contentPath);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected DocumentMetadata move(DocumentMetadata docMd, String toContentPath) throws IOException {
		
		DocumentStore ds = storeFactory.getDocumentStore(docMd);
		
		DocumentMetadata docMD = ds.move(docMd, toContentPath);
		
		docMD = docRepo.save(docMD);
		
		return docMD;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public DocumentMetadata delete(DocumentMetadata docMd) throws IOException {
		
		DocumentStore ds = storeFactory.getDocumentStore(docMd);
		
		ds.delete(docMd);
		
		docMd.setDeleted(true);
		docMd = docRepo.save(docMd);
		
		return docMd;
	}
	
	@Override
	public DocumentMetadata getDocumentMetadata(String docIdentity) {
		return docRepo.findByIdentity(docIdentity);
	}

}
