package com.enablix.app.report.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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

import com.enablix.app.report.activity.metric.ActivityMetricService;
import com.enablix.core.domain.report.activitymetric.ActivityMetricType;

@RestController
@RequestMapping("report")
public class ActivityMetricController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ActivityMetricController.class);

	@Autowired
	private ActivityMetricService activityMetric;

	@RequestMapping(method = RequestMethod.GET, value="/activitymetric")
	public HashMap<String, Object> getAggregatedActivityMetrices(@RequestParam("activityMetricTime") String date) {
		try{
			Date startDate = new SimpleDateFormat("dd-MMM-yy").parse(date);
			Date endDate = Calendar.getInstance().getTime();
			HashMap<String, Object> response = new HashMap<String, Object>();
			response.put("metricData", activityMetric.getAggregatedValues(startDate, endDate, null));
			response.put("asOfDate", endDate);
			return response;
		}
		catch(Exception e){
			LOGGER.error(" Error Retrieving the Metric Data",e);
			return null;
		}
	}
	
	@RequestMapping(value = "/getactivitymetrices", method = RequestMethod.GET, produces = "application/json")
	public List<ActivityMetricType> getActivityMetricesTypes() {
		return ActivityMetricType.getAllTypes();
	}
}
