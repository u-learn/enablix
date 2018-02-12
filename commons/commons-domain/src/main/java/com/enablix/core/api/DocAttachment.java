package com.enablix.core.api;

import java.io.InputStream;

public class DocAttachment {

	private String docQId;
	
	private InputStream inputStream;
	
	private String filename;
	
	private String contentType;
	
	private boolean temporary;
	
	private long filesize;

	public String getDocQId() {
		return docQId;
	}

	public void setDocQId(String docQId) {
		this.docQId = docQId;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public boolean isTemporary() {
		return temporary;
	}

	public void setTemporary(boolean temporary) {
		this.temporary = temporary;
	}

	public long getFilesize() {
		return filesize;
	}

	public void setFilesize(long filesize) {
		this.filesize = filesize;
	}
	
}
