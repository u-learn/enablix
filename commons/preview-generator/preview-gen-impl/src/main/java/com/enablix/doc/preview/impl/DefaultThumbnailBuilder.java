package com.enablix.doc.preview.impl;

import org.springframework.stereotype.Component;

import com.enablix.commons.dms.api.DocumentStore;
import com.enablix.core.api.DocInfo;
import com.enablix.core.api.DocumentFormat;
import com.enablix.doc.preview.ThumbnailBuilder;
import com.enablix.doc.preview.ThumbnailSize;

@Component
public class DefaultThumbnailBuilder implements ThumbnailBuilder {

	@Override
	public DocInfo createAndSaveThumbnail(ThumbnailSize size, DocInfo thumbnailFrom, DocumentStore<?, ?> sourceDocStore,
			DocumentStore<?, ?> destDocStore) {
		return thumbnailFrom;
	}

	@Override
	public boolean canBuildFrom(DocumentFormat from) {
		return from == DocumentFormat.PNG;
	}

}
