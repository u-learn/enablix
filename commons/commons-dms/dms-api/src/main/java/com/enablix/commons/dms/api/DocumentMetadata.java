package com.enablix.commons.dms.api;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.api.DocInfo;
import com.enablix.core.domain.BaseDocumentEntity;

@Document(collection = "disk_document_metadata_ebx")
public abstract class DocumentMetadata extends BaseDocumentEntity implements DocInfo {

	public static final String PREVIEW_STATUS_FLD_ID = "previewStatus";
	
	public enum PreviewStatus {
		PENDING, AVAILABLE, NOT_SUPPORTED, FAILED
	}
	
	private String name;
	
	private String contentType;
	
	private String contentQId;
	
	private boolean deleted;
	
	private boolean temporary;
	
	private PreviewStatus previewStatus;
	
	private String embedHtml;
	
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
		this.previewStatus = PreviewStatus.PENDING;
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

	public boolean isTemporary() {
		return temporary;
	}

	public void setTemporary(boolean temporary) {
		this.temporary = temporary;
	}

	public PreviewStatus getPreviewStatus() {
		return previewStatus;
	}

	public void setPreviewStatus(PreviewStatus previewStatus) {
		this.previewStatus = previewStatus;
	}

	public String getEmbedHtml() {
		return embedHtml;
	}

	public void setEmbedHtml(String embedUrl) {
		this.embedHtml = embedUrl;
	}
	
}
