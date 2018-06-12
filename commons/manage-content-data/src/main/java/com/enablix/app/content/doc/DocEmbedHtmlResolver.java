package com.enablix.app.content.doc;

import com.enablix.commons.dms.api.DocumentMetadata;

public interface DocEmbedHtmlResolver {

	String getEmbedHtml(DocumentMetadata docMd);

	boolean canHandle(DocumentMetadata docMd);
	
}
