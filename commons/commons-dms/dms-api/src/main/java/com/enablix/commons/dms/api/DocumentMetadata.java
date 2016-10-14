package com.enablix.commons.dms.api;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.domain.BaseDocumentEntity;

@Document(collection = "disk_document_metadata_ebx")
public abstract class DocumentMetadata extends BaseDocumentEntity {

	private String name;
	
	private String contentType;
	
	private String contentQId;
	
	private boolean deleted;
	
	protected DocumentMetadata(String docName, String contentType, 
			String contentQId) {
		this(null, docName, contentType, contentQId);
	}
	
	public DocumentMetadata(String identity, String name, String contentType, 
			String contentQId) {
		
		setIdentity(identity);
		this.name = name;
		this.contentType = contentType;
		this.contentQId = contentQId;
		this.deleted = false;
	}
	
	public String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	public String getContentType() {
		return contentType;
	}

	public String getContentQId() {
		return contentQId;
	}

	public void setContentQId(String contentQId) {
		this.contentQId = contentQId;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

}
