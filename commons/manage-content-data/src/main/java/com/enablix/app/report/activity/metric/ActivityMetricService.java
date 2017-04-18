package com.enablix.app.report.activity.metric;

import java.util.Date;
import java.util.List;

import com.enablix.core.domain.report.activitymetric.ActivityMetricConfig;
import com.enablix.core.domain.report.activitymetric.ReportStats;

public interface ActivityMetricService {
	
	List<ActivityMetricConfig> getActivityMetricConfig();

	List<ReportStats> getAggregatedValues(Date date);
	
	ReportStats executeActivityMetrices(ActivityMetricConfig activityMetrices);
	
}