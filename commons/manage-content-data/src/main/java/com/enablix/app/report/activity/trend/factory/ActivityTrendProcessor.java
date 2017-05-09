package com.enablix.app.report.activity.trend.factory;

import java.util.Date;
import java.util.List;

import com.enablix.core.domain.report.activitymetric.MetricStats;

public interface ActivityTrendProcessor {

	List<MetricStats> getTrendData(Date startDate, Date endDate);

}