package com.enablix.app.report.activity.metric;

import java.util.Date;

import com.enablix.commons.constants.AppConstants.MetricTypes;
import com.enablix.core.domain.report.activitymetric.MetricStats;

public interface MetricStatsCalculator {
	
	MetricTypes getActivityCode();
	
	MetricStats calculateStats(Date startDate, Date endDate);
	
}