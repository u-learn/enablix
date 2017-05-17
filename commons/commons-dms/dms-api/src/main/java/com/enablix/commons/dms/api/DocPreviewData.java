package com.enablix.commons.dms.api;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.api.DocInfo;
import com.enablix.core.domain.BaseDocumentEntity;

@Document( collection = "ebx_doc_preview_data")
public abstract class DocPreviewData extends BaseDocumentEntity {

	public enum PreviewType {
		IMAGE
	}
	
	private String docIdentity;
	
	private DocInfo smallThumbnail;
	
	private DocInfo largeThumbnail;
	
	private PreviewType type;
	
	public DocPreviewData(PreviewType type) {
		this.type = type;
	}

	public String getDocIdentity() {
		return docIdentity;
	}

	public void setDocIdentity(String docIdentity) {
		this.docIdentity = docIdentity;
	}

	public DocInfo getSmallThumbnail() {
		return smallThumbnail;
	}

	public void setSmallThumbnail(DocInfo sThumbnail) {
		this.smallThumbnail = sThumbnail;
	}

	public DocInfo getLargeThumbnail() {
		return largeThumbnail;
	}

	public void setLargeThumbnail(DocInfo lThumbnail) {
		this.largeThumbnail = lThumbnail;
	}

	public PreviewType getType() {
		return type;
	}
	
	public abstract List<DocInfo> getParts();
	
}
