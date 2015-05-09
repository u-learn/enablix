package com.enablix.commons.dms.disk;

import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.util.id.IdentityUtil;

public class DiskDocumentMetadata extends DocumentMetadata {

	// Path of the file stored on server disk
	private String location;
	
	private long contentLength;
	
	private DiskDocumentMetadata() { 
		super(null, null);
	}
	
	protected DiskDocumentMetadata(String name, String contentType) {
		this(null, name, contentType);
		this.setIdentity(IdentityUtil.generateIdentity(this));
	}

	public DiskDocumentMetadata(String identity, String name, String contentType) {
		super(identity, name, contentType);
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public long getContentLength() {
		return contentLength;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}
	
}
