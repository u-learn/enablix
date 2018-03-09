package com.enablix.doc.preview;

import java.io.IOException;

import com.enablix.commons.dms.api.DocumentStore;
import com.enablix.core.api.DocInfo;
import com.enablix.core.api.DocumentFormat;

public interface ThumbnailBuilder {

	DocInfo createAndSaveThumbnail(ThumbnailSize size, DocInfo thumbnailFrom, DocumentStore<?, ?> sourceDocStore, DocumentStore<?, ?> destDocStore) throws IOException;

	boolean canBuildFrom(DocumentFormat from);
	
}
