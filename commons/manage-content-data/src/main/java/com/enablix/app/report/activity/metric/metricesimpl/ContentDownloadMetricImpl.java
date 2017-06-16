package com.enablix.app.report.activity.metric.metricesimpl;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.enablix.commons.constants.AppConstants.MetricType;

@Component
public class ContentDownloadMetricImpl extends ActivityMetric {
	
	private Criteria docDownloadCriteria = Criteria.where("activity.activityType").is("DOC_DOWNLOAD");

	@Override
	public MetricType metricType() {
		return MetricType.CONTENT_DOWNLOAD;
	}

	@Override
	protected Criteria baseCriteria() {
		return docDownloadCriteria;
	}

}
