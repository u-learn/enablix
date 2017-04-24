package com.enablix.trigger.lifecycle.scheduler;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.enablix.commons.constants.AppConstants;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.domain.tenant.Tenant;
import com.enablix.core.domain.trigger.ContentChange;
import com.enablix.core.domain.trigger.LifecycleCheckpoint;
import com.enablix.core.domain.trigger.LifecycleCheckpoint.ExecutionStatus;
import com.enablix.core.system.repo.TenantRepository;
import com.enablix.trigger.lifecycle.CheckpointExecutor;
import com.enablix.trigger.lifecycle.repo.LifecycleCheckpointRepository;

@Component
public class LifecycleCheckpointExecScheduler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LifecycleCheckpointExecScheduler.class);

	@Autowired
	private TenantRepository tenantRepo;
	
	@Autowired
	private LifecycleCheckpointRepository checkpointRepo;
	
	@Autowired
	private CheckpointExecutor<ContentChange> checkpointExecutor;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Scheduled(cron = "${lifecycle.checkpoint.exec.cron}")
	public void executeLifecycleCheckpoint() {
		
		// TODO: check for already running jobs
		
		Date now = Calendar.getInstance().getTime();
		
		List<Tenant> allTenants = tenantRepo.findAll();
		
		for (Tenant tenant : allTenants) {
			
			LOGGER.debug("executing checkpoint job for tenant [{}] with execution time [{}]", tenant.getTenantId(), now);
			
			try {
				
				ProcessContext.initialize(AppConstants.SYSTEM_USER_ID, 
						AppConstants.SYSTEM_USER_NAME, tenant.getTenantId(), tenant.getDefaultTemplateId(), null);
				
				List<LifecycleCheckpoint> checkpoints = 
						checkpointRepo.findByStatusAndScheduledExecDateBefore(ExecutionStatus.PENDING, now);
				
				for (LifecycleCheckpoint checkpoint : checkpoints) {
					checkpointExecutor.execute(checkpoint);
				}
				
			} catch (Throwable t) {
				
				LOGGER.error("Error executing checkpoint job for tenant [" + tenant.getTenantId() + "]", t);
				
			} finally {
				ProcessContext.clear();
			}
		}
		
	}
	
}
