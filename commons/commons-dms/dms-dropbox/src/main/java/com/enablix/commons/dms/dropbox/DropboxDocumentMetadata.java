package com.enablix.commons.dms.dropbox;

import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.util.id.IdentityUtil;

public class DropboxDocumentMetadata extends DocumentMetadata {

	private long contentLength;
	
	private String location;
	
	private DropboxDocumentMetadata() {
		super(null, null, null);
	}
	
	protected DropboxDocumentMetadata(String docName, String contentType) {
		this(null, docName, contentType, null);
		setIdentity(IdentityUtil.generateIdentity(this));
	}
	
	protected DropboxDocumentMetadata(String docName, String contentType, 
			String contentQId) {
		this(null, docName, contentType, contentQId);
		setIdentity(IdentityUtil.generateIdentity(this));
	}

	public DropboxDocumentMetadata(String identity, String name, String contentType, 
			String contentQId) {
		super(identity, name, contentType, contentQId);
	}

	public long getContentLength() {
		return contentLength;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
}
