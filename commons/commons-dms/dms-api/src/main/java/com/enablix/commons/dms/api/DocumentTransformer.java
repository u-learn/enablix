package com.enablix.commons.dms.api;

import java.io.IOException;
import java.util.List;

import com.enablix.core.api.DocInfo;
import com.enablix.core.api.DocumentFormat;

public interface DocumentTransformer {

	<SDM extends DocumentMetadata, SD extends Document<SDM>>

	List<DocInfo> convertAndSaveToDocStore(DocInfo docInfo, DocumentFormat toFormat, String toContentType,
			String storeBaseLocation, DocumentStore<SDM, SD> sourceDocStore, DocumentStore<?, ?> targetDocStore,
			DocumentTxStrategy docTxStrategy, DocNameStrategy docNameStrategy) throws IOException;
	
	
}
