package com.enablix.commons.dms.api;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.domain.BaseDocumentEntity;

@Document(collection = "disk_document_metadata_ebx")
public abstract class DocumentMetadata extends BaseDocumentEntity {

	private String name;
	
	private String contentType;
	
	protected DocumentMetadata(String docName, String contentType) {
		this(null, docName, contentType);
	}
	
	public DocumentMetadata(String identity, String name, String contentType) {
		setIdentity(identity);
		this.name = name;
		this.contentType = contentType;
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

}
