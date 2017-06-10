package com.enablix.app.report.activity.metric;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.enablix.core.domain.report.activitymetric.ActivityMetricConfig;
import com.enablix.core.domain.report.activitymetric.MetricStats;

public interface ActivityMetricService {
	
	List<ActivityMetricConfig> getActivityMetricConfig();

	List<MetricStats> getAggregatedValues(Date startDate, Date endDate) throws ParseException;
	
}