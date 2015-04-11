package com.enablix.commons.dms.disk;

public interface DiskStoreLocationResolver {

	String getDocumentStoragePath(DiskDocumentMetadata metadata);
	
}
