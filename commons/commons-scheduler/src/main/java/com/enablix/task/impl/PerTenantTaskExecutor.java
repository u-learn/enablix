package com.enablix.task.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.constants.AppConstants;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.domain.scheduler.TaskConfig;
import com.enablix.core.domain.tenant.Tenant;
import com.enablix.core.system.repo.TenantRepository;
import com.enablix.task.RunnableTask;

@Component
public class PerTenantTaskExecutor extends SimpleTaskExecutor {

	private static final Logger LOGGER = LoggerFactory.getLogger(PerTenantTaskExecutor.class);
	
	@Autowired
	private TenantRepository tenantRepo;
	
	@Override
	public void run(List<RunnableTask> tasks) {
		
		List<Tenant> allTenants = tenantRepo.findAll();
		
		for (Tenant tenant : allTenants) {
			
			String tenantId = tenant.getTenantId();
			
			for (RunnableTask task : tasks) {
				
				TaskConfig taskConfig = task.getTaskConfig();
				List<String> tenantFilter = taskConfig.getTenantFilter();
				
				if (CollectionUtil.isEmpty(tenantFilter) || tenantFilter.contains(tenantId)) {
				
					LOGGER.debug("executing scheduled task [{}] for tenant [{}]", 
							taskConfig.getName(), tenantId);
				
					try {
						
						ProcessContext.initialize(AppConstants.SYSTEM_USER_ID, 
								AppConstants.SYSTEM_USER_NAME, tenantId, tenant.getDefaultTemplateId(), null);
						
						run(task);
						
					} catch (Throwable t) {
						
						LOGGER.error("Error executing checkpoint job for tenant [" + tenantId + "]", t);
						
					} finally {
						ProcessContext.clear();
					}
					
				}
			}
		}
		
	}

}
