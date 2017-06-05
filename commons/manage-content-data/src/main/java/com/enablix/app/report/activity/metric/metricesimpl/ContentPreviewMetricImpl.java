package com.enablix.app.report.activity.metric.metricesimpl;

import java.util.Date;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.enablix.commons.constants.AppConstants.MetricTypes;
import com.enablix.commons.util.date.DateUtil;
import com.enablix.core.domain.report.activitymetric.MetricStats;

@Component
public class ContentPreviewMetricImpl extends SimpleMetric {
	
	private final MetricTypes activityCode = MetricTypes.CONTENT_PREVIEW;

	private final String collectionName = "ebx_activity_audit";

	private Criteria contentPreviewCriteria = Criteria.where("activity.activityType").is("DOC_PREVIEW");
	
	@Override
	public MetricStats calculateStats(Date startDate, Date endDate) {

		startDate = DateUtil.getStartOfDay(startDate);
		endDate = DateUtil.getEndOfDay(endDate);
		
		Criteria criteria = Criteria.where("createdAt").gte(startDate).lte(endDate);
		criteria = criteria.andOperator(contentPreviewCriteria);
		return calculateSimpleMetric(startDate, endDate, 
				collectionName, activityCode, criteria);

	}

	@Override
	public MetricTypes getActivityCode() {
		return activityCode;
	}
}
