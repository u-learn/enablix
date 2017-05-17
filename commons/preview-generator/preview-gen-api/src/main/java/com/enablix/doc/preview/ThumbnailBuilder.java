package com.enablix.doc.preview;

import com.enablix.commons.dms.api.DocumentStore;
import com.enablix.core.api.DocInfo;
import com.enablix.core.api.DocumentFormat;

public interface ThumbnailBuilder {

	DocInfo createAndSaveThumbnail(ThumbnailSize size, DocInfo thumbnailFrom, DocumentStore<?, ?> sourceDocStore, DocumentStore<?, ?> destDocStore);

	boolean canBuildFrom(DocumentFormat from);
	
}
