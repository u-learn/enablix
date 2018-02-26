package com.enablix.app.report.activity.trend;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ActivityTrendService {
	
	List<Map<String, Long>> getActivityTrends(Date startDate, Date endDate, List<String> activityMetrices, String trendType) throws ParseException;
	
}