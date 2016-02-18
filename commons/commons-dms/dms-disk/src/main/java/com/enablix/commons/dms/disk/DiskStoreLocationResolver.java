package com.enablix.commons.dms.disk;

import java.io.IOException;

public interface DiskStoreLocationResolver {

	String getDocumentStoragePath(DiskDocumentMetadata metadata, String contentPath) throws IOException;
	
}
