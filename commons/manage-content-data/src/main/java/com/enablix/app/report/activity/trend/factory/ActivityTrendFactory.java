package com.enablix.app.report.activity.trend.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.constants.AppConstants.ActivityTrend;

@Component
public class ActivityTrendFactory {
	
	@Autowired
	private DailyTrendProcessor dailyTrendProcessor;

	@Autowired
	private WeeklyTrendProcessor weeklyTrendProcessor;
	
	@Autowired
	private MonthlyTrendProcessor monthlyTrendProcessor;

	@Autowired
	private YearlyTrendProcessor yearlyTrendProcessor;
	
	public ActivityTrendProcessor getTrendProcessor(String trendType) {
		
		if(ActivityTrend.DAILY.getValue().equalsIgnoreCase(trendType)) {
			return dailyTrendProcessor;
		}
		else if(ActivityTrend.WEEKLY.getValue().equalsIgnoreCase(trendType)) {
			return weeklyTrendProcessor;
		}
		else if(ActivityTrend.MONTHLY.getValue().equalsIgnoreCase(trendType)) {
			return monthlyTrendProcessor;
		}
		else if(ActivityTrend.YEARLY.getValue().equalsIgnoreCase(trendType)) {
			return yearlyTrendProcessor;
		}
		throw new IllegalArgumentException(" No Implementation Found for the Trend "+trendType);
	
	}
}
