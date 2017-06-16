package com.enablix.app.report.activity.metric.metricesimpl;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.enablix.commons.constants.AppConstants.MetricType;

@Component
public class ContentAddMetricImpl extends ActivityMetric {

	private Criteria contentAddCriteria = Criteria.where("activity.activityType").is("CONTENT_ADD");
	
	@Override
	public MetricType metricType() {
		return MetricType.CONTENT_ADD;
	}

	@Override
	protected Criteria baseCriteria() {
		return contentAddCriteria;
	}

}
