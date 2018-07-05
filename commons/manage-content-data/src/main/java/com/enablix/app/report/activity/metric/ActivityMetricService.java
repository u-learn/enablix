package com.enablix.app.report.activity.metric;

import java.util.Date;
import java.util.List;

import com.enablix.core.api.SearchRequest;
import com.enablix.core.domain.report.activitymetric.MetricStats;

public interface ActivityMetricService {
	
	List<MetricStats> getAggregatedValues(Date startDate, Date endDate, SearchRequest userFilter);
	
}