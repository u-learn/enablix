package com.enablix.doc.converter;

import com.enablix.core.api.DocumentFormat;

public interface DocConverterFactory {

	DocConverter getConverter(DocumentFormat from, DocumentFormat to);
	
}
