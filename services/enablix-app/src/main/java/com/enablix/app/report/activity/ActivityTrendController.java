package com.enablix.app.report.activity;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.app.report.activity.trend.ActivityTrendService;

@RestController
@RequestMapping("activitytrend")
public class ActivityTrendController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ActivityTrendController.class);

	@Autowired
	ActivityTrendService activityTrend;
	
	@RequestMapping(method = RequestMethod.GET)
	public List<Map<String, Integer>> getAggregatedActivityMetrices(@RequestParam("activityMetricTime") String date, 
			@RequestParam("activityMetric") List<String> activityMetrices) {
		try{
			return activityTrend.getActivityTrends(date, activityMetrices);
			
		}
		catch(Exception e){
			LOGGER.error(" Error Retrieving the Trend Data",e);
			return null;
		}
	}
}