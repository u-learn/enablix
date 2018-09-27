package com.enablix.app.content.doc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.enablix.app.content.ContentDataPathResolver;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.dms.DocumentStoreConstants;
import com.enablix.commons.dms.api.Document;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.dms.api.DocumentMetadata.PreviewStatus;
import com.enablix.commons.dms.api.DocumentStore;
import com.enablix.commons.dms.repository.DocumentMetadataRepository;
import com.enablix.commons.util.QIdUtil;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.commons.util.concurrent.Events;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.DocInfo;
import com.enablix.core.api.IDocument;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.mongo.dao.GenericDao;
import com.enablix.core.mongo.view.MongoDataView;
import com.enablix.core.mq.Event;
import com.enablix.core.mq.util.EventUtil;
import com.enablix.doc.preview.DocPreviewService;

@Component
public class DocumentManagerImpl implements DocumentManager {

	private static final String TEMP_SUB_FOLDER = "tmp";
	private static final String THUMBNAIL_SUB_FOLDER = "thumbnails";
	
	@Autowired
	private DocumentStoreFactory storeFactory;
	
	@Autowired
	private DocumentMetadataRepository docRepo;
	
	@Autowired
	private ContentDataPathResolver pathResolver;
	
	@Autowired
	private TemplateManager templateMgr;
	
	@Autowired
	private GenericDao genericDao;
	
	@Autowired
	private DocPreviewService docPreviewService;
	
	@Autowired
	private DocEmbedHtmlResolverFactory embedHtmlResolverFactory;
	
	@Autowired
	private DocReferenceSupervisorFactory docRefSupervisorFactory;
	
	@Override
	public DocumentMetadata saveUsingParentInfo(Document<?> doc, String docContainerQId, 
			String docContainerParentInstanceIdentity, boolean generatePreview,
			boolean generatePreviewAsync) throws IOException {
		
		String contentPath = createContentPathUsingParentInfo(docContainerQId, 
				docContainerParentInstanceIdentity, doc.getMetadata().isTemporary(), false);
		return save(doc, contentPath, generatePreview, generatePreviewAsync);
	}

	private String createContentPathUsingParentInfo(String docContainerQId, 
			String docContainerParentInstanceIdentity, boolean temporaryDoc, boolean thumbnailDoc) {
		
		String templateId = ProcessContext.get().getTemplateId();
		String contentDataPath = pathResolver.resolveContainerPath(templateId, docContainerQId);
		
		if (thumbnailDoc) {
			contentDataPath = pathResolver.appendPath(contentDataPath, THUMBNAIL_SUB_FOLDER);
		}
		
		if (temporaryDoc) {
			contentDataPath = pathResolver.appendPath(contentDataPath, TEMP_SUB_FOLDER);
		}
		
		return contentDataPath;
	}

	@Override
	public DocumentMetadata saveUsingContainerInfo(Document<?> doc, String docContainerQId, 
			String docContainerInstanceIdentity, boolean generatePreview,
			boolean generatePreviewAsync) throws IOException {
		
		String contentPath = createContentPathUsingContainerInfo(docContainerQId, 
				docContainerInstanceIdentity, doc.getMetadata().isTemporary(), false);
		return save(doc, contentPath, generatePreview, generatePreviewAsync);
	}

	private String createContentPathUsingContainerInfo(String docContainerQId, 
			String docContainerInstanceIdentity, boolean temporaryDoc, boolean thumbnailDoc) {
		
		String templateId = ProcessContext.get().getTemplateId();
		String contentDataPath = pathResolver.resolveContainerPath(templateId, docContainerQId);
		
		if (thumbnailDoc) {
			contentDataPath = pathResolver.appendPath(contentDataPath, THUMBNAIL_SUB_FOLDER);
		}
		
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
	public DocumentMetadata save(Document<?> doc, String contentPath, 
			boolean generatePreview, boolean generatePreviewAsync) throws IOException {
		
		DocumentStore ds = storeFactory.getDocumentStore(doc);
		
		DocumentMetadata docMD = ds.save(doc, contentPath);
		
		docMD.setPreviewStatus(PreviewStatus.PENDING); 
		
		String embedHtml = null;
		
		DocEmbedHtmlResolver resolver = embedHtmlResolverFactory.getResolver(docMD);
		if (resolver != null) {
			embedHtml = resolver.getEmbedHtml(docMD);
		}
		
		docMD.setEmbedHtml(embedHtml);
		docMD = docRepo.save(docMD);
		
		if (generatePreview) {
			
			/*if (generatePreviewAsync) {
			
				EventUtil.publishEvent(new Event<DocumentMetadata>(Events.GENERATE_DOC_PREVIEW, docMD));
				
			} else {
				docMD = docPreviewService.createPreview(docMD);
			}*/
			// TODO: handle generate async
			// While the preview is still being generated, if user submits the records, save fails as the
			// system tries to move the file from tmp folder to actual folder and gets an error: File in
			// use by other program. Need to handle this.
			docMD = docPreviewService.createPreview(docMD);
		}
		
		EventUtil.publishEvent(new Event<DocumentMetadata>(Events.DOCUMENT_UPLOADED, docMD));
		
		return docMD;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Document<DocumentMetadata> buildDocument(InputStream dataStream, String name, 
			String contentType, String contentQId, long contentLength, String docIdentity, 
			boolean tmpDoc, boolean thumbnailUpload) {
		
		String storeType = thumbnailUpload ? storeFactory.defaultStoreType() : 
			storeFactory.getStoreType(DocumentStoreConstants.DEFAULT_DOC_STORE_CONFIG_PROP);
		
		DocumentStore ds = storeFactory.getDocumentStore(storeType);
		return ds.getDocumentBuilder().build(dataStream, name, contentType, contentQId, contentLength, docIdentity, tmpDoc);
	}
	
	@Override
	public DocumentMetadata attachUsingContainerInfo(DocumentMetadata docMd, String docContainerQId,
			String docContainerInstanceIdentity, boolean thumbnailDoc) throws IOException {
		
		String contentPath = createContentPathUsingContainerInfo(docContainerQId, 
				docContainerInstanceIdentity, false, thumbnailDoc);
		
		docMd.setTemporary(false);
		
		return move(docMd, contentPath);
	}

	@Override
	public DocumentMetadata attachUsingParentInfo(DocumentMetadata docMd, String docContainerQId,
			String docContainerParentInstanceIdentity, boolean thumbnailDoc) throws IOException {
		
		String contentPath = createContentPathUsingParentInfo(docContainerQId, 
				docContainerParentInstanceIdentity, false, thumbnailDoc);
		
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

	@SuppressWarnings("rawtypes")
	@Override
	public IDocument load(DocInfo docInfo, String storeType) throws IOException {
		DocumentStore docStore = storeFactory.getDocumentStore(storeType);
		return docStore.load(docInfo);
	}

	@Override
	public DocumentMetadata updatePreviewStatus(String docIdentity, PreviewStatus status) {
		
		return updateDocMetadataAttr(
				docIdentity, DocumentMetadata.PREVIEW_STATUS_FLD_ID, 
				(docMd) -> docMd.setPreviewStatus(status), status);

	}
	
	@Override
	public DocumentMetadata updateEmbedHtml(String docIdentity, String embedHtml) {
		
		return updateDocMetadataAttr(
					docIdentity, DocumentMetadata.EMBED_HTML_FLD_ID, 
					(docMd) -> docMd.setEmbedHtml(embedHtml), embedHtml);
	}
	
	private DocumentMetadata updateDocMetadataAttr(String docIdentity, 
			String attrId, Consumer<DocumentMetadata> updater, Object attrValue) {
		
		DocumentMetadata docMetadata = getDocumentMetadata(docIdentity);
		
		if (docMetadata != null) {
			
			updater.accept(docMetadata);
			docMetadata = docRepo.save(docMetadata);
			
			Collection<DocReferenceSupervisor> supervisors = docRefSupervisorFactory.getSupervisors();
			for (DocReferenceSupervisor supervisor : supervisors) {
				supervisor.updateDocMetadataAttr(docMetadata, attrId, attrValue);
			}
			
		}
		
		return docMetadata;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map<String, Object> getReferenceRecord(String docQId, String docIdentity) {
		
		Map<String, Object> record = null;
		
		if (StringUtil.hasText(docQId) && StringUtil.hasText(docIdentity)) {
			
			TemplateFacade template = templateMgr.getTemplateFacade(ProcessContext.get().getTemplateId());
	
			String parentQId = QIdUtil.getParentQId(docQId);
			String parentCollName = template.getCollectionName(parentQId);
			
			if (StringUtil.hasText(parentCollName)) {
				
				String docAttrId = QIdUtil.getElementId(docQId);
				String docIdentityAttr = docAttrId + "." + ContentDataConstants.IDENTITY_KEY;
	
				Criteria criteria = Criteria.where(docIdentityAttr).is(docIdentity);
				
				List<Map> records = genericDao.findByCriteria(criteria, parentCollName, Map.class, MongoDataView.ALL_DATA);
				if (CollectionUtil.isNotEmpty(records)) {
					record = records.get(0);
				}
			}
			
		}
		
		return record;
	}

	@Override
	public DocumentMetadata updateContentQId(String docIdentity, String contentQId) {

		return updateDocMetadataAttr(
				docIdentity, ContentDataConstants.CONTENT_QID_KEY, 
				(docMd) -> docMd.setContentQId(contentQId), contentQId);
	}

	@Override
	public DocumentMetadata saveThumnailDoc(Document<?> document, String containerQId, 
			String docContainerParentInstanceIdentity,
			boolean generatePreview, boolean generatePreviewAsync) throws IOException {
		
		String contentPath = createContentPathUsingParentInfo(containerQId, 
				docContainerParentInstanceIdentity, document.getMetadata().isTemporary(), true);
		return save(document, contentPath, generatePreview, generatePreviewAsync);
	}

}
