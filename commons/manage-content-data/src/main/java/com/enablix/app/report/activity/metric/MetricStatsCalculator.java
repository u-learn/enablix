package com.enablix.app.report.activity.metric;

import java.util.Date;
import java.util.List;

import com.enablix.commons.constants.AppConstants.MetricType;
import com.enablix.core.api.SearchRequest;
import com.enablix.core.domain.report.activitymetric.MetricStats;

public interface MetricStatsCalculator {
	
	MetricType metricType();
	
	MetricStats calculateStats(Date startDate, Date endDate, SearchRequest userFilter);
	
	List<MetricStats> calculateDailyTrend(Date startDate, Date endDate);
	
	List<MetricStats> calculateWeeklyTrend(Date startDate, Date endDate);

	List<MetricStats> calculateMonthlyTrend(Date startDate, Date endDate);
}