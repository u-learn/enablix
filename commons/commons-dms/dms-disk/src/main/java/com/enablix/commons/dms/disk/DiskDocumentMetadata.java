package com.enablix.commons.dms.disk;

import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.util.id.IdentityUtil;

public class DiskDocumentMetadata extends DocumentMetadata {

	// Path of the file stored on server disk
	private String location;
	
	protected DiskDocumentMetadata(String name) {
		this(null, name);
		this.setIdentity(IdentityUtil.generateIdentity(this));
	}
	
	public DiskDocumentMetadata(String identity, String name) {
		super(identity, name);
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
}
