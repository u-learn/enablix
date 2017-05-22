package com.enablix.doc.converter;

import java.io.IOException;
import java.io.InputStream;

import com.enablix.core.api.DocumentFormat;

public interface DocConverter {

	void convertAndWrite(InputStream is, DocumentFormat inFormat, DocumentStream os, DocumentFormat outFormat) throws IOException;
	
	boolean canConvert(DocumentFormat from, DocumentFormat to);
	
}
