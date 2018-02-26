package com.enablix.app.report.activity.metric;

import java.util.Date;
import java.util.List;

import com.enablix.core.domain.report.activitymetric.MetricStats;
import com.enablix.core.mongo.search.service.SearchRequest;

public interface ActivityMetricService {
	
	List<MetricStats> getAggregatedValues(Date startDate, Date endDate, SearchRequest userFilter);
	
}