package com.enablix.doc.preview;

import com.enablix.core.api.DocumentFormat;

public interface ThumbnailBuilderFactory {

	ThumbnailBuilder getBuilder(DocumentFormat from);
	
}
