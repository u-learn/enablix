package com.enablix.app.report.activity.metric.metricesimpl;

import java.util.Date;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.enablix.commons.constants.AppConstants.MetricTypes;
import com.enablix.commons.util.date.DateUtil;
import com.enablix.core.domain.report.activitymetric.MetricStats;

@Component
public class ContentDownloadMetricImpl extends SimpleMetric {
	
	private final MetricTypes activityCode = MetricTypes.CONTENT_DOWNLOAD;

	private final String collectionName = "ebx_activity_audit";

	private Criteria docDownloadCriteria = Criteria.where("activity.activityType").is("DOC_DOWNLOAD");

	
	@Override
	public MetricStats calculateStats(Date startDate, Date endDate) {
		
		startDate = DateUtil.getStartOfDay(startDate);
		endDate = DateUtil.getEndOfDay(endDate);
		
		Criteria criteria = Criteria.where("createdAt").gte(startDate).lte(endDate);
		criteria = criteria.andOperator(docDownloadCriteria);
		return calculateSimpleMetric(startDate, endDate, 
				collectionName, activityCode, criteria);
	
	}

	@Override
	public MetricTypes getActivityCode() {
		return activityCode;
	}
	
}
