package com.enablix.commons.dms.api;

import java.io.InputStream;

public interface DocumentBuilder<DM extends DocumentMetadata, D extends Document<DM>> {

	D build(InputStream dataStream, String name, String contentType, 
			String contentQId, long contentLength, String docIdentity);
}
