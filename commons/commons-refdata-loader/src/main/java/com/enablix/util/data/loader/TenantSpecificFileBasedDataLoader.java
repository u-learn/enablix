package com.enablix.util.data.loader;

import java.io.File;

import com.enablix.commons.util.process.ProcessContext;

public class TenantSpecificFileBasedDataLoader extends FileBasedDataLoader {
	
	public TenantSpecificFileBasedDataLoader(String dataDirectory, String[] fileExtns,
			DataFileProcessor fileProcessor) {
		super(dataDirectory, fileExtns, fileProcessor);
	}

	protected void loadDataFile(File dataFile) {

		String tenantId = resolveTenantIdFromFile(dataFile);

		ProcessContext.initialize("system", tenantId, null);
		
		try {
			
			super.loadDataFile(dataFile);
			
		} finally {
			ProcessContext.clear();
		}
	}

	private String resolveTenantIdFromFile(File templateFile) {
		return templateFile.getParentFile().getName();
	}
	
}
