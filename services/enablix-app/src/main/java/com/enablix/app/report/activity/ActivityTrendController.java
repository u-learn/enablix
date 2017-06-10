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

import com.enablix.app.report.activity.trend.ActivityTrendService;
import com.enablix.commons.util.date.DateUtil;

@RestController
@RequestMapping("activitytrend")
public class ActivityTrendController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ActivityTrendController.class);

	@Autowired
	private ActivityTrendService activityTrend;
	
	@RequestMapping(method = RequestMethod.GET)
	public HashMap<String, Object> getAggregatedActivityMetrices(@RequestParam("activityMetricTime") String date, 
			@RequestParam("activityMetric") List<String> activityMetrices, @RequestParam("activityMetricTrend") String activityMetricTrend) {
		try{
			Date startDate = new SimpleDateFormat("dd-MMM-yy").parse(date);
			Date endDate = Calendar.getInstance().getTime();
			
			HashMap<String, Object> response = new HashMap<String, Object>();
			response.put("trendData", activityTrend.getActivityTrends(startDate, endDate ,activityMetrices, activityMetricTrend));
			response.put("asOfDate", DateUtil.getPreviousDate());
			return response;
		}
		catch(Exception e){
			LOGGER.error(" Error Retrieving the Trend Data",e);
			return null;
		}
	}
}