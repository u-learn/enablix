package com.enablix.doc.preview;

import java.io.IOException;

import com.enablix.commons.dms.api.DocPreviewData;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.core.api.IDocument;

public interface DocPreviewService {

	void createPreview(String docIdentity) throws IOException;

	DocPreviewData getPreviewData(String docIdentity);

	IDocument getPreviewDataPart(String docIdentity, int elementIndx) throws IOException;
	
	IDocument getDocSmallThumbnail(String docIdentity) throws IOException;
	
	IDocument getDocLargeThumbnail(String docIdentity) throws IOException;

	void createPreview(DocumentMetadata docMetadata) throws IOException;
	
}
