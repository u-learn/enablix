package com.enablix.app.report.activity.metric;

import java.util.Date;
import java.util.List;

import com.enablix.commons.constants.AppConstants.MetricType;
import com.enablix.core.domain.report.activitymetric.MetricStats;
import com.enablix.core.mongo.search.service.SearchRequest;

public interface MetricStatsCalculator {
	
	MetricType metricType();
	
	MetricStats calculateStats(Date startDate, Date endDate, SearchRequest userFilter);
	
	List<MetricStats> calculateDailyTrend(Date startDate, Date endDate);
	
	List<MetricStats> calculateWeeklyTrend(Date startDate, Date endDate);

	List<MetricStats> calculateMonthlyTrend(Date startDate, Date endDate);
}