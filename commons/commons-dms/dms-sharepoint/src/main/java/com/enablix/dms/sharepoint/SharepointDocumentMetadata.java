package com.enablix.dms.sharepoint;

import com.enablix.commons.dms.api.ContentLengthAwareDocument;
import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.util.id.IdentityUtil;

public class SharepointDocumentMetadata extends DocumentMetadata implements ContentLengthAwareDocument {

	private String fileLocation;
	
	private long contentLength;
	
	private SharepointDocumentMetadata() {
		super(null, null, null);
	}
	
	protected SharepointDocumentMetadata(String docName, String contentType, String contentQId) {
		this(null, docName, contentType, contentQId);
		setIdentity(IdentityUtil.generateIdentity(this));
	}

	public SharepointDocumentMetadata(String identity, String name, String contentType, String contentQId) {
		super(identity, name, contentType, contentQId);
	}
	
	@Override
	public long getContentLength() {
		return contentLength;
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

}
