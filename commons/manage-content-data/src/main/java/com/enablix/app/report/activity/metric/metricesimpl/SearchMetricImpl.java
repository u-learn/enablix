package com.enablix.app.report.activity.metric.metricesimpl;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.enablix.commons.constants.AppConstants.MetricType;

@Component
public class SearchMetricImpl extends ActivityMetric {

	private Criteria searchCriteria = Criteria.where("activity.category").is("SEARCH");
	
	@Override
	public MetricType metricType() {
		return MetricType.SEARCH;
	}

	@Override
	protected Criteria baseCriteria() {
		return searchCriteria;
	}

}
