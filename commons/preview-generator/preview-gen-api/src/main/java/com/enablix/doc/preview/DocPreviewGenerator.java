package com.enablix.doc.preview;

import java.io.IOException;

import com.enablix.commons.dms.api.DocPreviewData;
import com.enablix.commons.dms.api.Document;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.dms.api.DocumentStore;

public interface DocPreviewGenerator {

	<SDM extends DocumentMetadata, SD extends Document<SDM>>
	DocPreviewData createPreview(SDM docMetadata, 
			DocumentStore<SDM, SD> sourceDocStore,
			DocumentStore<? , ?> previewDocStore) throws IOException;
	
}
