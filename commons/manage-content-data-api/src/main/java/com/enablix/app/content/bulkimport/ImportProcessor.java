package com.enablix.app.content.bulkimport;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.enablix.commons.exception.ValidationException;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.domain.content.ImportRecord;
import com.enablix.core.domain.content.ImportRequest;

public interface ImportProcessor {

	String importSource();

	void validateRequest(ImportRequest request) throws ValidationException;

	ImportProcessedInfo processRecord(ImportRecord record, ContainerType container, ImportContext ctx) throws IOException;
	
	
	public static class ImportProcessedInfo {
		
		private ImportDoc importDoc;
		
		private Map<String, Object> recordAttributes;

		public ImportProcessedInfo() { }
		
		
		public ImportProcessedInfo(ImportDoc importDoc, Map<String, Object> recordAttributes) {
			super();
			this.importDoc = importDoc;
			this.recordAttributes = recordAttributes;
		}

		public ImportDoc getImportDoc() {
			return importDoc;
		}

		public void setImportDoc(ImportDoc importDoc) {
			this.importDoc = importDoc;
		}

		public Map<String, Object> getRecordAttributes() {
			return recordAttributes;
		}

		public void setRecordAttributes(Map<String, Object> recordAttributes) {
			this.recordAttributes = recordAttributes;
		}
		
	}
	
	
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
