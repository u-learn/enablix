package com.enablix.dms.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import com.enablix.commons.dms.api.DocNameStrategy;
import com.enablix.commons.util.IOUtil;
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
	public void endPageWriting(Map<String, Object> pageProps) throws IOException {
		
		IOUtil.closeStream(currentOS);
		
		LengthAwareInputStream<IS> is = outStreamCreator.convertToInputStream(currentOS);
		IS inputStream = is.getInputStream();
		
		try {
			docWriter.saveDocument(inputStream, is.getContentLength(), 
					docNameStrategy.nextPageDocName(pageNum), pageProps);
		} finally {
			IOUtil.closeStream(inputStream);
		}
	}

	@Override
	public void writingError(Exception e) {
		IOUtil.closeStream(currentOS);
	}
	
	@Override
	public void endDocumentWriting(Map<String, Object> docProps) {
		IOUtil.closeStream(currentOS);
	}

}
