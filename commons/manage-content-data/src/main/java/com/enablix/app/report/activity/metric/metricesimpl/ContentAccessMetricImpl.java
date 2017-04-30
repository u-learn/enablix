package com.enablix.app.report.activity.metric.metricesimpl;

import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.report.activity.metric.utils.MetricAggService;
import com.enablix.commons.constants.AppConstants.MetricTypes;
import com.enablix.core.domain.report.activitymetric.MetricStats;

@Component
public class ContentAccessMetricImpl extends SimpleMetric {
	
	@Autowired
	private MetricAggService metricAggService;

	private final MetricTypes activityCode = MetricTypes.CONTENT_ACCESS;

	private final String collectionName = "ebx_activity_audit";

	@Override
	public MetricStats calculate(Date executionDate) {

		HashMap<String, String> fields = new HashMap<String, String>();
		fields.put("activity.activityType", "CONTENT_ACCESS");
		return calculateSimpleMetric(executionDate, 
				collectionName, activityCode, fields);

	}

	@Override
	public MetricTypes getActivityCode() {
		return activityCode;
	}

	@Override
	public MetricStats getAggStats(Date startDate) {
		return metricAggService.getSimpleAggStats(startDate, activityCode);
	}

}
