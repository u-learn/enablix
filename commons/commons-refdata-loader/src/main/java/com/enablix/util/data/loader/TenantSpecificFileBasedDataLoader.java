package com.enablix.util.data.loader;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;

import com.enablix.commons.constants.AppConstants;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.domain.tenant.Tenant;
import com.enablix.core.system.repo.TenantRepository;

public class TenantSpecificFileBasedDataLoader extends FileBasedDataLoader {
	
	@Autowired
	private TenantRepository tenantRepo;
	
	public TenantSpecificFileBasedDataLoader(String dataDirectory, String[] fileExtns,
			DataFileProcessor fileProcessor) {
		super(dataDirectory, fileExtns, fileProcessor);
	}

	protected void loadDataFile(File dataFile) {

		String tenantId = resolveTenantIdFromFile(dataFile);

		Tenant tenant = tenantRepo.findByTenantId(tenantId);
		
		if (tenant == null) {
			throw new IllegalArgumentException("Invalid tenant id: " + tenantId);
		}
		
		String templateId = tenant.getDefaultTemplateId();
		
		ProcessContext.initialize(AppConstants.SYSTEM_USER_ID, 
				AppConstants.SYSTEM_USER_NAME, tenantId, templateId, null);
		
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
