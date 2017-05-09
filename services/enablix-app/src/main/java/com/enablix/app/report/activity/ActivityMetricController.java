package com.enablix.app.report.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.app.report.activity.metric.ActivityMetricConfigRepository;
import com.enablix.app.report.activity.metric.ActivityMetricService;
import com.enablix.commons.util.date.DateUtil;
import com.enablix.core.domain.report.activitymetric.ActivityMetricConfig;

@RestController
@RequestMapping("activitymetric")
public class ActivityMetricController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ActivityMetricController.class);

	@Autowired
	private ActivityMetricService activityMetric;

	@Autowired
	private ActivityMetricConfigRepository activityMetricConfRepo;
	
	@RequestMapping(method = RequestMethod.GET)
	public HashMap<String, Object> getAggregatedActivityMetrices(@RequestParam("activityMetricTime") String date) {
		try{
			Date startDate = new SimpleDateFormat("dd-MMM-yy").parse(date);
			Date endDate = DateUtil.getPreviousDate();
			HashMap<String, Object> response = new HashMap<String, Object>();
			response.put("metricData", activityMetric.getAggregatedValues(startDate, endDate));
			response.put("asOfDate", endDate);
			return response;
		}
		catch(Exception e){
			LOGGER.error(" Error Retrieving the Metric Data",e);
			return null;
		}
	}
	
	@RequestMapping(value = "/getactivitymetrices", method = RequestMethod.GET, produces = "application/json")
	public List<ActivityMetricConfig> getActivityMetricesTypes() {
		try{
			return activityMetricConfRepo.findAll();
		}
		catch(Exception e){
			LOGGER.error(" Error Retrieving the list of Metrics Config ",e);
			return null;
		}
	}
}
