package com.enablix.commons.dms.disk.impl;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.enablix.commons.dms.disk.DiskDocumentMetadata;
import com.enablix.commons.dms.disk.DiskStoreLocationResolver;
import com.enablix.commons.util.tenant.TenantUtil;

@Component
public class DefaultDiskStoreLocationResolver implements DiskStoreLocationResolver {

	@Value("${doc.disk.storage.location:/usr/local/applications/enablix/docstore}")
	private String storeBaseLocation;
	
	@Override
	public String getDocumentStoragePath(DiskDocumentMetadata metadata, String contentPath) {
		
		String docFolderPath = storeBaseLocation + File.separator 
				+ TenantUtil.getTenantId() + File.separator + contentPath;
		
		File docFolder = new File(docFolderPath);
		
		if (!docFolder.exists()) {
			docFolder.mkdirs();
		}
		
		return docFolderPath + File.separator + metadata.getName();
	}

}
