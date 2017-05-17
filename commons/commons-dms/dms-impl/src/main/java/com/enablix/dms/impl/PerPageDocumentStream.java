package com.enablix.dms.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.enablix.commons.dms.api.DocNameStrategy;
import com.enablix.dms.impl.StreamManager.LengthAwareInputStream;
import com.enablix.doc.converter.DocumentStream;

public class PerPageDocumentStream<OS extends OutputStream, IS extends InputStream> implements DocumentStream {

	private StreamManager<OS, IS> outStreamCreator;
	
	private DocWriter docWriter;
	
	private DocNameStrategy docNameStrategy;
	
	private OS currentOS;
	
	private int pageNum;
	
	public PerPageDocumentStream(StreamManager<OS, IS> outStreamCreator, 
			DocWriter docWriter, DocNameStrategy docNameStrategy) {
		
		super();
		this.outStreamCreator = outStreamCreator;
		this.docWriter = docWriter;
		this.docNameStrategy = docNameStrategy;
		this.pageNum = 0;
	}
	
	@Override
	public OutputStream nextPageOutputStream() {
		currentOS = outStreamCreator.createOutputStream();
		return currentOS;
	}

	@Override
	public void startDocumentWriting() {
		this.pageNum = 0;
	}

	@Override
	public void startPageWriting() throws IOException {
		this.pageNum++;
	}

	@Override
	public void endPageWriting() throws IOException {
		
		currentOS.close();
		
		LengthAwareInputStream<IS> is = outStreamCreator.convertToInputStream(currentOS);
		docWriter.saveDocument(is.getInputStream(), is.getContentLength(), 
				docNameStrategy.nextPageDocName(pageNum));
	}

	@Override
	public void writingError(Exception e) {
		// empty implementation
	}

	@Override
	public void endDocumentWriting() {
		// empty implementation
	}

}
