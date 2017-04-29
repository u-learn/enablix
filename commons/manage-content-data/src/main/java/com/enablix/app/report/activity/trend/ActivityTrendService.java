package com.enablix.app.report.activity.trend;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface ActivityTrendService {
	
	List<Map<String,Integer>> getActivityTrends(String date, List<String> activityMetrices) throws ParseException;

}