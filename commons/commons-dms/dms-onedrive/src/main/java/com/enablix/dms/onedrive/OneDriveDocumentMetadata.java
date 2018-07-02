package com.enablix.dms.onedrive;

import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.util.id.IdentityUtil;
import com.enablix.core.api.ContentLengthAwareDocument;

public class OneDriveDocumentMetadata extends DocumentMetadata implements ContentLengthAwareDocument {

	private String fileLocation;
	
	private long contentLength;
	
	private String driveFileId;
	
	private String driveParentId;
	
	private OneDriveDocumentMetadata() {
		super(null, null, null);
	}
	
	protected OneDriveDocumentMetadata(String docName, String contentType) {
		this(null, docName, contentType, null);
		setIdentity(IdentityUtil.generateIdentity(this));
	}
	
	protected OneDriveDocumentMetadata(String docName, String contentType, String contentQId) {
		this(null, docName, contentType, contentQId);
		setIdentity(IdentityUtil.generateIdentity(this));
	}

	public OneDriveDocumentMetadata(String identity, String name, String contentType, String contentQId) {
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

	@Override
	public String getLocation() {
		return getFileLocation();
	}

	@Override
	public void setLocation(String location) {
		setFileLocation(location);
	}

	public String getDriveFileId() {
		return driveFileId;
	}

	public void setDriveFileId(String driveFileId) {
		this.driveFileId = driveFileId;
	}

	public String getDriveParentId() {
		return driveParentId;
	}

	public void setDriveParentId(String driveParentId) {
		this.driveParentId = driveParentId;
	}
	
}
