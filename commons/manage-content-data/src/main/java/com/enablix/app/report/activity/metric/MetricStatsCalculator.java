package com.enablix.app.report.activity.metric;

import java.util.Date;
import java.util.List;

import com.enablix.commons.constants.AppConstants.MetricTypes;
import com.enablix.core.domain.report.activitymetric.MetricStats;

public interface MetricStatsCalculator {
	
	MetricTypes getActivityCode();
	
	MetricStats calculateStats(Date startDate, Date endDate);
	
	List<MetricStats> calculateDailyTrend(Date startDate, Date endDate);
	
	List<MetricStats> calculateWeeklyTrend(Date startDate, Date endDate);

	List<MetricStats> calculateMonthlyTrend(Date startDate, Date endDate);
}