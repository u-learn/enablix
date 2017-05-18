package com.enablix.doc.converter.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Map;

import com.enablix.commons.util.IOUtil;
import com.enablix.doc.converter.DocumentStream;

public class TempDiskLocDocumentStream implements DocumentStream {

	private String tmpLocation;
	private String fileName;
	private File outFile;
	private FileOutputStream fos;
	
	public TempDiskLocDocumentStream(String tmpLocation, String fileName) {
		super();
		this.tmpLocation = tmpLocation;
		this.fileName = fileName;
	}

	@Override
	public OutputStream nextPageOutputStream() throws IOException {
		
		if (fos == null) {
			
			long currentTime = Calendar.getInstance().getTimeInMillis();
			outFile = new File(tmpLocation + "/" + fileName + "." + currentTime);
			if (outFile.exists()) {
				outFile.delete();
			}
			
			fos = new FileOutputStream(outFile);
		}
		
		return fos;
	}

	@Override
	public void startDocumentWriting() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startPageWriting() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endPageWriting(Map<String, Object> pageProps) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writingError(Exception e) {
		IOUtil.closeStream(fos);
		if (outFile != null) {
			outFile.delete();
		}
	}

	@Override
	public void endDocumentWriting(Map<String, Object> docProps) {
		IOUtil.closeStream(fos);
	}

	public File getOutFile() {
		return outFile;
	}

}
