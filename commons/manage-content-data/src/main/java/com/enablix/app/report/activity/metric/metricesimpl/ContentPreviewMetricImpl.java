package com.enablix.app.report.activity.metric.metricesimpl;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.enablix.commons.constants.AppConstants.MetricType;

@Component
public class ContentPreviewMetricImpl extends ActivityMetric {
	
	private Criteria contentPreviewCriteria = Criteria.where("activity.activityType").is("DOC_PREVIEW");
	
	@Override
	public MetricType metricType() {
		return MetricType.CONTENT_PREVIEW;
	}

	@Override
	protected Criteria baseCriteria() {
		return contentPreviewCriteria;
	}


}
