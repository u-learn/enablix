package com.enablix.app.report.activitymetric;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.app.report.activity.metric.ActivityMetricService;
import com.enablix.core.domain.report.activitymetric.ReportStats;

@RestController
@RequestMapping("activitymetric")
public class ActivityMetricController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ActivityMetricController.class);
	

	@Autowired
	ActivityMetricService activityMetric;

	@RequestMapping(method = RequestMethod.GET)
	public List<ReportStats> getAggregatedActivityMetrices(@RequestParam("activityMetricTime") String date) {
		try{
			Date activityMetricDate = new SimpleDateFormat("dd-MMM-yy").parse(date);
			return activityMetric.getAggregatedValues(activityMetricDate);
		}
		catch(Exception e){
			LOGGER.error(" Error Retrieving the Metric Data",e);
			return null;
		}
	}
}