package com.enablix.app.content.doc.impl;

import org.springframework.stereotype.Component;

import com.enablix.app.content.doc.DocEmbedHtmlResolver;
import com.enablix.app.content.doc.DocEmbedHtmlResolverFactory;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.util.beans.SpringBackedAbstractFactory;

@Component
public class DocEmbedHtmlResolverFactoryImpl extends SpringBackedAbstractFactory<DocEmbedHtmlResolver> implements DocEmbedHtmlResolverFactory {

	@Override
	public DocEmbedHtmlResolver getResolver(DocumentMetadata docMd) {
		for (DocEmbedHtmlResolver resolver : registeredInstances()) {
			if (resolver.canHandle(docMd)) {
				return resolver;
			}
		}
		return null;
	}

	@Override
	protected Class<DocEmbedHtmlResolver> lookupForType() {
		return DocEmbedHtmlResolver.class;
	}

}
