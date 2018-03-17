package com.enablix.analytics.app;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.enablix.analytics.web.AnalyticsController.BayesExecRequest;
import com.enablix.bayes.BayesService;
import com.enablix.bayes.CsvRelevanceRecorder;
import com.enablix.bayes.DBRelevanceRecorder;
import com.enablix.bayes.ExecutionContext;
import com.enablix.bayes.RelevanceRecorder;
import com.enablix.bayes.content.ContentBayesNet;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.domain.tenant.Tenant;
import com.enablix.core.system.repo.TenantRepository;

@Component
public class BayesExecutor {
	
	@Value("${bayes.output.dir}")
	private String outputDir;

	@Autowired
	private TenantRepository tenantRepo;
	
	@Autowired
	private BayesService bayesService;
	
	@Autowired
	private DBRelevanceRecorder dbRecorder;
	
	public void run(BayesExecRequest request) throws Exception {
		
		List<String> forTenants = request.getForTenants();
		List<Tenant> tenants = forTenants == null ? tenantRepo.findAll() : 
								tenantRepo.findByIdentityIn(forTenants);
		
		if (!CollectionUtil.isEmpty(tenants)) {
		
			Date runAsStartDate = request.getRunAsStartDate();
			
			Calendar runAs = Calendar.getInstance();
			if (runAsStartDate != null) {
				runAs.setTime(runAsStartDate);
			}
			
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
			
				bayesService.runBatch(ctx, ContentBayesNet.build(), getRecorder(request));
				
				// go to next date
				runAs.add(Calendar.DAY_OF_YEAR, 1);
				
			} while (runAsEndDate != null && !runAs.after(runAsEndDate));
			
			
		}
	}
	
	private RelevanceRecorder getRecorder(BayesExecRequest request) {
		
		switch(request.getRecorderType()) {
		
			case DB:
				return dbRecorder;
			
			default:
				return new CsvRelevanceRecorder(outputDir);
		}
	}

}
