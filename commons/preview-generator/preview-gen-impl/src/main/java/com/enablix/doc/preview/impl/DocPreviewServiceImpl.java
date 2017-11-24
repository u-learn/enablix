package com.enablix.doc.preview.impl;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.doc.DocumentManager;
import com.enablix.app.content.doc.DocumentStoreFactory;
import com.enablix.commons.dms.DocumentStoreConstants;
import com.enablix.commons.dms.api.DocPreviewData;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.dms.api.DocumentMetadata.PreviewStatus;
import com.enablix.commons.dms.api.DocumentStore;
import com.enablix.core.api.DocInfo;
import com.enablix.core.api.IDocument;
import com.enablix.doc.preview.DocPreviewGenerator;
import com.enablix.doc.preview.DocPreviewService;
import com.enablix.doc.preview.crud.DocPreviewDataCrudService;

@Component
public class DocPreviewServiceImpl implements DocPreviewService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DocPreviewServiceImpl.class);
	
	@Autowired
	private DocumentManager docManager;
	
	@Autowired
	private DocumentStoreFactory docStoreFactory;
	
	@Autowired
	private DocPreviewGenerator previewGenerator;
	
	@Autowired
	private DocPreviewDataCrudService previewDataCrud;
	
	@Override
	public void createPreview(String docIdentity) throws IOException {
		
		DocumentMetadata docMetadata = docManager.getDocumentMetadata(docIdentity);
		
		if (docMetadata == null) {
			LOGGER.error("Document metadata not found for identity: {}", docIdentity);
			throw new IllegalArgumentException("Document metadata missing [" + docIdentity + "]");
		}
	
		createPreview(docMetadata);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public DocumentMetadata createPreview(DocumentMetadata docMetadata) throws IOException {
		
		// find the source doc store for the document
		DocumentStore sourceDocStore = docStoreFactory.getDocumentStore(docMetadata);
		
		// find the preview doc store
		String previewStoreType = previewStoreType();
		DocumentStore previewDocStore = docStoreFactory.getDocumentStore(previewStoreType);
		
		// generate preview
		DocPreviewData previewData = previewGenerator.createPreview(docMetadata, sourceDocStore, previewDocStore);
		DocumentMetadata docMd = null;
		
		if (previewData != null) {
			// save doc preview data
			previewDataCrud.saveOrUpdate(previewData);
			
			// update preview status
			docMd = docManager.updatePreviewStatus(docMetadata.getIdentity(), PreviewStatus.AVAILABLE);
		} else {
			docMd = docManager.updatePreviewStatus(docMetadata.getIdentity(), PreviewStatus.NOT_SUPPORTED);
		}
		
		return docMd;
	}

	private String previewStoreType() {
		return docStoreFactory.getStoreType(DocumentStoreConstants.PREVIEW_DOC_STORE_CONFIG_PROP);
	}

	@Override
	public DocPreviewData getPreviewData(String docIdentity) {
		return previewDataCrud.getRepository().findByDocIdentity(docIdentity);
	}

	@Override
	public IDocument getPreviewDataPart(String docIdentity, int elementIndx) throws IOException {

		IDocument resultDoc = null;
		
		DocPreviewData previewData = getPreviewData(docIdentity);
		
		if (previewData != null) {
		
			List<DocInfo> parts = previewData.getParts();
			
			if (parts.size() > elementIndx) {
				DocInfo partDocInfo = parts.get(elementIndx);
				resultDoc = docManager.load(partDocInfo, previewStoreType());
			}
		}
		
		return resultDoc;
	}

	@Override
	public IDocument getDocSmallThumbnail(String docIdentity) throws IOException {
		return getThumbnail(docIdentity, (docPD) -> docPD.getSmallThumbnail());
	}

	private IDocument getThumbnail(String docIdentity, Function<DocPreviewData, DocInfo> getThumbnailDoc) throws IOException {
		
		IDocument resultDoc = null;
		DocPreviewData previewData = getPreviewData(docIdentity);
		
		if (previewData != null) {

			DocInfo docInfo = getThumbnailDoc.apply(previewData);
			if (docInfo != null) {
				resultDoc = docManager.load(docInfo, previewStoreType());
			}
		}
		
		return resultDoc;
	}

	@Override
	public IDocument getDocLargeThumbnail(String docIdentity) throws IOException {
		return getThumbnail(docIdentity, (docPD) -> docPD.getLargeThumbnail());
	}
	
}
