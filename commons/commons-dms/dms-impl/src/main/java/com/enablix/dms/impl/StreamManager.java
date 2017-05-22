package com.enablix.dms.impl;

import java.io.InputStream;
import java.io.OutputStream;

public interface StreamManager<OS extends OutputStream, IS extends InputStream> {

	OS createOutputStream();

	LengthAwareInputStream<IS> convertToInputStream(OS outputStream);
	
	public interface LengthAwareInputStream<IS extends InputStream> {
		
		public IS getInputStream();

		public long getContentLength();
		
	}
	
}
