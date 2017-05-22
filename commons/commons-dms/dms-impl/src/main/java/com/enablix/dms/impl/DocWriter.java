package com.enablix.dms.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public interface DocWriter {

	void saveDocument(InputStream inputStream, long contentLength, 
			String docName, Map<String, Object> docProps) throws IOException;

}
