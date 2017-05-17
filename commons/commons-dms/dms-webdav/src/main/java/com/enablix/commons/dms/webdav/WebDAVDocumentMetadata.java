package com.enablix.commons.dms.webdav;

import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.util.id.IdentityUtil;
import com.enablix.core.api.ContentLengthAwareDocument;

public class WebDAVDocumentMetadata extends DocumentMetadata implements ContentLengthAwareDocument {
	
	private String fileLocation;
	
	private long contentLength;

	private WebDAVDocumentMetadata() {
		super(null, null, null);
	}
	
	WebDAVDocumentMetadata(String docName, String contentType, 
			String contentQId) {
		this(null, docName, contentType, contentQId);
		setIdentity(IdentityUtil.generateIdentity(this));
	}
	
	public WebDAVDocumentMetadata(String identity, String name, String contentType, String contentQId) {
		super(identity, name, contentType, contentQId);
	}
	
	public String getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

	@Override
	public long getContentLength() {
		return contentLength;
	}

	@Override
	public String getLocation() {
		return getFileLocation();
	}

	@Override
	public void setLocation(String location) {
		setFileLocation(location);
	}

}
