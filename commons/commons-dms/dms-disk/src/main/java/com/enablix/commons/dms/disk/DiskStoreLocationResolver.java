package com.enablix.commons.dms.disk;

import java.io.IOException;

import com.enablix.core.api.DocInfo;

public interface DiskStoreLocationResolver {

	String getDocumentStoragePath(DocInfo metadata, String contentPath) throws IOException;
	
}
