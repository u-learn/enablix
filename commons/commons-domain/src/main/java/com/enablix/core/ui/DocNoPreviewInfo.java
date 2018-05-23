package com.enablix.core.ui;

public class DocNoPreviewInfo extends ContentPreviewInfo {

	private String docType;
	
	public DocNoPreviewInfo() {
		super(PreviewType.DOC_NO_PREVIEW);
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

}
