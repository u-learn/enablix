package com.enablix.app.content.bulkimport;

import java.io.IOException;
import java.io.InputStream;

import com.enablix.commons.exception.ValidationException;
import com.enablix.core.domain.content.ImportRecord;
import com.enablix.core.domain.content.ImportRequest;

public interface ImportProcessor {

	String importSource();

	void validateRequest(ImportRequest request) throws ValidationException;

	ImportDoc processRecord(ImportRecord record, ImportContext ctx) throws IOException;
	
	public static class ImportDoc {
		
		private String filename;
		
		private String mimeType;
		
		private InputStream inputStream;
		
		public ImportDoc() {
			
		}

		public ImportDoc(String filename, String mimeType, InputStream inputStream) {
			super();
			this.filename = filename;
			this.mimeType = mimeType;
			this.inputStream = inputStream;
		}

		public String getFilename() {
			return filename;
		}

		public void setFilename(String filename) {
			this.filename = filename;
		}

		public String getMimeType() {
			return mimeType;
		}

		public void setMimeType(String mimeType) {
			this.mimeType = mimeType;
		}

		public InputStream getInputStream() {
			return inputStream;
		}

		public void setInputStream(InputStream inputStream) {
			this.inputStream = inputStream;
		}
		
		
	}
	
}
