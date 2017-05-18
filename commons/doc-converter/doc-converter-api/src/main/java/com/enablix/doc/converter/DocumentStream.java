package com.enablix.doc.converter;

import java.io.IOException;
import java.io.OutputStream;

public interface DocumentStream {
	
	OutputStream nextPageOutputStream() throws IOException;
	
	void startDocumentWriting();
	
	void startPageWriting() throws IOException;
	
	void endPageWriting() throws IOException;

	void writingError(Exception e);
	
	void endDocumentWriting();
	
}
