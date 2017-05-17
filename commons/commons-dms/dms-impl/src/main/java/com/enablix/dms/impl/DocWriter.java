package com.enablix.dms.impl;

import java.io.IOException;
import java.io.InputStream;

public interface DocWriter {

	void saveDocument(InputStream inputStream, long contentLength, String docName) throws IOException;

}
