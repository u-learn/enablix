package com.enablix.doc.converter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public interface DocumentStream {
	
	OutputStream nextPageOutputStream() throws IOException;
	
	void startDocumentWriting();
	
	void startPageWriting() throws IOException;
	
	void endPageWriting(Map<String, Object> imageProp) throws IOException;

	void writingError(Exception e);
	
	void endDocumentWriting(Map<String, Object> hashMap);
	
}
