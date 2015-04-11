package com.enablix.commons.dms.api;

public abstract class DocumentMetadata {

	private String identity;
	
	private String name;

	protected DocumentMetadata(String docName) {
		this(null, docName);
	}
	
	public DocumentMetadata(String identity, String name) {
		this.identity = identity;
		this.name = name;
	}
	
	public String getIdentity() {
		return identity;
	}

	protected void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}
	
}
