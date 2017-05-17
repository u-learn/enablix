package com.enablix.doc.preview.impl;

import org.springframework.stereotype.Component;

import com.enablix.commons.util.beans.SpringBackedAbstractFactory;
import com.enablix.core.api.DocumentFormat;
import com.enablix.doc.preview.ThumbnailBuilder;
import com.enablix.doc.preview.ThumbnailBuilderFactory;

@Component
public class ThumbnailBuilderFactoryImpl extends SpringBackedAbstractFactory<ThumbnailBuilder> implements ThumbnailBuilderFactory{

	@Override
	public ThumbnailBuilder getBuilder(DocumentFormat from) {
		
		for (ThumbnailBuilder builder : registeredInstances()) {
		
			if (builder.canBuildFrom(from)) {
				return builder;
			}
		}
		
		return null;
	}

	@Override
	protected Class<ThumbnailBuilder> lookupForType() {
		return ThumbnailBuilder.class;
	}

}
