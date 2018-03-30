package com.enablix.analytics.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.bayes.exec.BayesExecRequest;
import com.enablix.bayes.exec.BayesExecutor;

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

}
