package com.enablix.app.report.activity.metric.metricesimpl;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.enablix.commons.constants.AppConstants.MetricType;

@Component
public class ContentUpdateMetricImpl extends ActivityMetric {

	private Criteria contentUpdateCriteria = Criteria.where("activity.activityType").is("CONTENT_UPDATE");

	@Override
	public MetricType metricType() {
		return MetricType.CONTENT_UPDATES;
	}

	@Override
	protected Criteria baseCriteria() {
		return contentUpdateCriteria;
	}

}
