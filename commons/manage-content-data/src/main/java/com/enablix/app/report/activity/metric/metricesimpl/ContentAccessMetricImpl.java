package com.enablix.app.report.activity.metric.metricesimpl;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.enablix.commons.constants.AppConstants.MetricType;

@Component
public class ContentAccessMetricImpl extends ActivityMetric {
	
	private final MetricType activityCode = MetricType.CONTENT_ACCESS;

	private Criteria contentAccCriteria = Criteria.where("activity.activityType").is("CONTENT_ACCESS");
	
	@Override
	public MetricType metricType() {
		return activityCode;
	}

	@Override
	protected Criteria baseCriteria() {
		return contentAccCriteria;
	}

}
