package com.enablix.commons.dms.google.drive;

import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.util.id.IdentityUtil;

public class GoogleDriveDocumentMetadata extends DocumentMetadata {

	private long contentLength;
	
	private String location;
	
	private String driveFileId;
	
	private String driveParentId;
	
	private GoogleDriveDocumentMetadata() {
		super(null, null, null);
	}
	
	protected GoogleDriveDocumentMetadata(String docName, String contentType) {
		this(null, docName, contentType, null);
		setIdentity(IdentityUtil.generateIdentity(this));
	}
	
	protected GoogleDriveDocumentMetadata(String docName, String contentType, 
			String contentQId) {
		this(null, docName, contentType, contentQId);
		setIdentity(IdentityUtil.generateIdentity(this));
	}

	public GoogleDriveDocumentMetadata(String identity, String name, String contentType, 
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

	public String getDriveFileId() {
		return driveFileId;
	}

	public void setDriveFileId(String gdriveFileId) {
		this.driveFileId = gdriveFileId;
	}

	public String getDriveParentId() {
		return driveParentId;
	}

	public void setDriveParentId(String gdriveParentId) {
		this.driveParentId = gdriveParentId;
	}
	
}
