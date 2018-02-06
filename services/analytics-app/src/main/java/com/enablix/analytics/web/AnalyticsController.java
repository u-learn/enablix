package com.enablix.analytics.web;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.analytics.app.BayesExecutor;

@RestController
@RequestMapping("analytics")
public class AnalyticsController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AnalyticsController.class);

	@Autowired
	private BayesExecutor executor;
	
	@RequestMapping(method = RequestMethod.POST,
			consumes = "application/json",
			value="/bayesnet")
	public void runContentBayesNet(@RequestBody BayesExecRequest request) throws Exception {
		
		LOGGER.debug("Executing analytic for {}", request);
		
		executor.run(request);
	}
		
	
	public static class BayesExecRequest {
		
		private List<String> forTenants;
		
		private List<String> forUsers;
		
		private Date runAsStartDate;
		
		private Date runAsEndDate;
		
		public Date getRunAsStartDate() {
			return runAsStartDate;
		}

		@DateTimeFormat(iso = ISO.DATE)
		public void setRunAsStartDate(Date runAsDate) {
			this.runAsStartDate = runAsDate;
		}
		
		public Date getRunAsEndDate() {
			return runAsEndDate;
		}

		@DateTimeFormat(iso = ISO.DATE)
		public void setRunAsEndDate(Date runAsEndDate) {
			this.runAsEndDate = runAsEndDate;
		}

		public List<String> getForTenants() {
			return forTenants;
		}
		
		public void setForTenants(List<String> forTenants) {
			this.forTenants = forTenants;
		}
		
		public List<String> getForUsers() {
			return forUsers;
		}
		
		public void setForUsers(List<String> forUsers) {
			this.forUsers = forUsers;
		}

		@Override
		public String toString() {
			return "BayesExecRequest [forTenants=" + forTenants + ", forUsers=" + forUsers + "]";
		}
		
	}

}
