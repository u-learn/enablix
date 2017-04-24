package com.enablix.core.mongo.util;

import java.util.Collection;

import com.enablix.commons.constants.AppConstants;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.domain.tenant.Tenant;

public class MultiTenantExecutor {

	public static void executeForEachTenant(Collection<Tenant> tenants, TenantTask task) {
		
		for (Tenant tenant : tenants) {
			
			ProcessContext.initialize(AppConstants.SYSTEM_USER_ID, AppConstants.SYSTEM_USER_NAME, 
					tenant.getTenantId(), tenant.getDefaultTemplateId(), null);
			
			try {
				
				task.execute();
				
			} finally {
				ProcessContext.clear();
			}
		}
	}
	
	public static interface TenantTask {
		void execute();
	}
	
}
