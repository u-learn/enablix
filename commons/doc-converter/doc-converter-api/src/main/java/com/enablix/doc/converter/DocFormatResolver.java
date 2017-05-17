package com.enablix.doc.converter;

import com.enablix.core.api.DocInfo;
import com.enablix.core.api.DocumentFormat;

public interface DocFormatResolver {

	DocumentFormat resolve(DocInfo docInfo);
	
}
