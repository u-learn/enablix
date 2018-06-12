package com.enablix.app.content.doc;

import com.enablix.commons.dms.api.DocumentMetadata;

public interface DocEmbedHtmlResolverFactory {

	DocEmbedHtmlResolver getResolver(DocumentMetadata docMd);
	
}
