package com.enablix.dms.impl;

import java.io.InputStream;
import java.io.OutputStream;

import com.enablix.commons.dms.api.DocNameStrategy;
import com.enablix.doc.converter.DocumentStream;

public class DocStreamBuilder {

	public static <OS extends OutputStream, IS extends InputStream> 
			DocumentStream perPageDocStream(StreamManager<OS, IS> putStreamCreator, 
					DocWriter docWriter, DocNameStrategy docNameStrategy) {
		
		return new PerPageDocumentStream<OS, IS>(putStreamCreator, docWriter, docNameStrategy);
	}
	
}
