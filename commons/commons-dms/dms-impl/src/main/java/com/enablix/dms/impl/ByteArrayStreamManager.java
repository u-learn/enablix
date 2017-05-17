package com.enablix.dms.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class ByteArrayStreamManager implements StreamManager<ByteArrayOutputStream, ByteArrayInputStream> {

	@Override
	public ByteArrayOutputStream createOutputStream() {
		return new ByteArrayOutputStream();
	}

	@Override
	public com.enablix.dms.impl.StreamManager.LengthAwareInputStream<ByteArrayInputStream> convertToInputStream(
			ByteArrayOutputStream outputStream) {
		
		byte[] bytes = outputStream.toByteArray();
		
		return new LengthAwareInputStream<ByteArrayInputStream>() {

			@Override
			public ByteArrayInputStream getInputStream() {
				return new ByteArrayInputStream(bytes);
			}

			@Override
			public long getContentLength() {
				return bytes.length;
			}
		};
	}

}
