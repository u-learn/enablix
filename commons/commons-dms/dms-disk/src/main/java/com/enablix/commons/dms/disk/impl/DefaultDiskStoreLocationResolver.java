package com.enablix.commons.dms.disk.impl;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.enablix.commons.dms.disk.DiskStoreLocationResolver;
import com.enablix.commons.util.tenant.TenantUtil;
import com.enablix.core.api.DocInfo;

@Component
public class DefaultDiskStoreLocationResolver implements DiskStoreLocationResolver {

	@Value("${doc.disk.storage.location:/usr/local/applications/enablix/docstore}")
	private String storeBaseLocation;
	
	@Override
	public String getDocumentStoragePath(DocInfo metadata, String contentPath) throws IOException {
		
		String tenantFolderPath = storeBaseLocation + "/" + TenantUtil.getTenantId();
		
		String docFolderPath = !contentPath.startsWith(tenantFolderPath) ? 
				(tenantFolderPath + "/" + contentPath) : contentPath;
		
		File docFolder = new File(docFolderPath);
		
		if (!docFolder.exists()) {
			boolean dirCreated = docFolder.mkdirs();
			if (!dirCreated) {
				throw new IOException("Unable to create folder structure: " + docFolderPath);
			}
		}
		
		return docFolderPath + File.separator + metadata.getName();
	}

}
