package com.enablix.commons.dms.api;

import java.io.InputStream;

import com.enablix.core.api.DocInfo;
import com.enablix.core.api.IDocument;

public class BasicDocument implements IDocument {

	private DocInfo docInfo;
	
	private InputStream dataStream;

	@SuppressWarnings("unused")
	private BasicDocument() {
		// for ORM
	}
	
	public BasicDocument(DocInfo docInfo, InputStream dataStream) {
		this.docInfo = docInfo;
		this.dataStream = dataStream;
	}
	
	public DocInfo getDocInfo() {
		return docInfo;
	}

	public void setDocInfo(DocInfo docInfo) {
		this.docInfo = docInfo;
	}

	public InputStream getDataStream() {
		return dataStream;
	}

	public void setDataStream(InputStream dataStream) {
		this.dataStream = dataStream;
	}
	
}
