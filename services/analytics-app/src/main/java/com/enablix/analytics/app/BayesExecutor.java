package com.enablix.analytics.app;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.analytics.web.AnalyticsController.BayesExecRequest;
import com.enablix.bayes.BayesService;
import com.enablix.bayes.ExecutionContext;
import com.enablix.bayes.content.ContentBayesNet;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.domain.tenant.Tenant;
import com.enablix.core.system.repo.TenantRepository;

@Component
public class BayesExecutor {

	@Autowired
	private TenantRepository tenantRepo;
	
	@Autowired
	private BayesService bayesService;
	
	public void run(BayesExecRequest request) throws Exception {
		
		List<Tenant> tenants = tenantRepo.findByIdentityIn(request.getForTenants());
		
		if (!CollectionUtil.isEmpty(tenants)) {
		
			Calendar runAs = Calendar.getInstance();
			runAs.setTime(request.getRunAsStartDate());
			
			Calendar runAsEndDate = null;
			
			if (request.getRunAsEndDate() != null) {
				runAsEndDate = Calendar.getInstance();
				runAsEndDate.setTime(request.getRunAsEndDate());
			}
			
			do {
				
				ExecutionContext ctx = new ExecutionContext();
				ctx.setExecuteForTenants(tenants);
				ctx.setRunAsDate(runAs.getTime());
				ctx.setExecuteForUsers(request.getForUsers());
			
				bayesService.runBatch(ctx, ContentBayesNet.build());
				
				// go to next date
				runAs.add(Calendar.DAY_OF_YEAR, 1);
				
			} while (runAsEndDate != null && !runAs.after(runAsEndDate));
			
			
		}
	}

}
