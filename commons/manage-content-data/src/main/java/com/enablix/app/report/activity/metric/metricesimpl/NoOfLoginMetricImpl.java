package com.enablix.app.report.activity.metric.metricesimpl;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.enablix.commons.constants.AppConstants.MetricType;

@Component
public class NoOfLoginMetricImpl extends ActivityMetric {

	private Criteria noOfLoginsCriteria = Criteria.where("activity.activityType").is("LOGIN");

	@Override
	public MetricType metricType() {
		return MetricType.NO_OF_LOGINS;
	}

	@Override
	protected Criteria baseCriteria() {
		return noOfLoginsCriteria;
	}

}
