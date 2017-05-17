package com.enablix.doc.converter.impl;

import org.springframework.stereotype.Component;

import com.enablix.commons.util.beans.SpringBackedAbstractFactory;
import com.enablix.core.api.DocumentFormat;
import com.enablix.doc.converter.DocConverter;
import com.enablix.doc.converter.DocConverterFactory;

@Component
public class DocConverterFactoryImpl extends SpringBackedAbstractFactory<DocConverter> implements DocConverterFactory {

	@Override
	public DocConverter getConverter(DocumentFormat from, DocumentFormat to) {
		
		for (DocConverter converter : registeredInstances()) {
		
			if (converter.canConvert(from, to)) {
				return converter;
			}
		}
		
		return null;
	}

	@Override
	protected Class<DocConverter> lookupForType() {
		return DocConverter.class;
	}

}
