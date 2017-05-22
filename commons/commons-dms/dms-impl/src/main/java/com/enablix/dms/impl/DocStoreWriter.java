package com.enablix.dms.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.enablix.commons.dms.api.BasicDocInfo;
import com.enablix.commons.dms.api.BasicDocument;
import com.enablix.commons.dms.api.DocumentStore;
import com.enablix.core.api.DocInfo;

public class DocStoreWriter implements DocWriter {

	private DocumentStore<?, ?> docStore;
	private String basePath;
	private String contentType;

	private List<DocInfo> docPages;
	
	public DocStoreWriter(DocumentStore<?, ?> docStore, String basePath, String contentType) {
		super();
		this.docStore = docStore;
		this.basePath = basePath;
		this.contentType = contentType;
		this.docPages = new ArrayList<>();
	}


	public List<DocInfo> getDocPages() {
		return docPages;
	}

	@Override
	public void saveDocument(InputStream inputStream, long contentLength, 
			String docName, Map<String, Object> docProps) throws IOException {
		
		BasicDocInfo docInfo = new BasicDocInfo();
		docInfo.setContentLength(contentLength);
		docInfo.setName(docName);
		docInfo.setLocation(basePath + "/" + docName);
		docInfo.setContentType(contentType);
		docInfo.addProperties(docProps);
		
		BasicDocument doc = new BasicDocument(docInfo, inputStream);
		
		DocInfo di = docStore.save(doc, basePath);
		docPages.add(di);
	}

}
