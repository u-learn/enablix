package com.enablix.app.content.doc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.enablix.commons.dms.api.Document;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.dms.api.DocumentMetadata.PreviewStatus;
import com.enablix.core.api.DocInfo;
import com.enablix.core.api.IDocument;

public interface DocumentManager {

	Document<DocumentMetadata> load(String docIdentity) throws IOException;
	
	IDocument load(DocInfo docInfo, String storeType) throws IOException;

	DocumentMetadata saveUsingParentInfo(Document<?> doc, String docContainerQId, 
			String docContainerParentInstanceIdentity, boolean generatePreview,
			boolean generatePreviewAsync) throws IOException;
	
	DocumentMetadata save(Document<?> doc, String contentPath, boolean generatePreview,
			boolean generatePreviewAsync) throws IOException;

	DocumentMetadata saveUsingContainerInfo(Document<?> doc, String docContainerQId, 
			String docContainerInstanceIdentity, boolean generatePreview, boolean generatePreviewAsync) throws IOException;
	
	Document<DocumentMetadata> buildDocument(InputStream dataStream, String name, String contentType, 
			String contentQId, long fileSize, String docIdentity, boolean temporaryDoc, boolean thumbnailUpload);

	DocumentMetadata loadDocMetadata(String docIdentity);
	
	DocumentMetadata attachUsingContainerInfo(DocumentMetadata docMd, 
			String docContainerQId, String docContainerInstanceIdentity, boolean thumbnailDoc) throws IOException;
	
	DocumentMetadata attachUsingParentInfo(DocumentMetadata docMd, String docContainerQId, 
			String docContainerParentInstanceIdentity, boolean thumbnailDoc) throws IOException;

	DocumentMetadata delete(DocumentMetadata docMd) throws IOException;

	DocumentMetadata getDocumentMetadata(String docIdentity);
	
	DocumentMetadata updatePreviewStatus(String docIdentity, PreviewStatus status);

	Map<String, Object> getReferenceRecord(String docQId, String docIdentity);

	DocumentMetadata updateEmbedHtml(String docIdentity, String embedHtml);

	DocumentMetadata updateContentQId(String docIdentity, String contentQId);

	DocumentMetadata saveThumnailDoc(Document<?> document, String containerQId, String docContainerParentInstanceIdentity,
			boolean generatePreview, boolean generatePreviewAsync) throws IOException;

}
