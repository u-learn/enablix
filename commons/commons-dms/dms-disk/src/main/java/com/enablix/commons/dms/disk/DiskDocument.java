package com.enablix.commons.dms.disk;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.enablix.commons.dms.api.ContentLengthAwareDocument;
import com.enablix.commons.dms.api.Document;
import com.enablix.commons.util.StringUtil;

public class DiskDocument extends Document<DiskDocumentMetadata> implements ContentLengthAwareDocument {

	private InputStream dataStream;
	
	public DiskDocument(InputStream dataStream, String name, String contentType, 
			String contentQId) {
		this(dataStream, new DiskDocumentMetadata(name, contentType, contentQId));
	}
	
	public DiskDocument(InputStream dataStream, DiskDocumentMetadata metadata) {
		this.dataStream = dataStream;
		setMetadata(metadata);
	}
	
	public DiskDocument(DiskDocumentMetadata metadata) {
		setMetadata(metadata);
	}

	@Override
	public InputStream getDataStream() {
		
		InputStream is = dataStream;
		
		if (is == null && !StringUtil.isEmpty(getMetadata().getLocation())) {
			try {
				is = new FileInputStream(getMetadata().getLocation());
			} catch (FileNotFoundException e) {
				throw new IllegalArgumentException("Unable to open file", e);
			}
		}
		
		return is;
	}

	@Override
	public long getContentLength() {
		return getMetadata().getContentLength();
	}

}
