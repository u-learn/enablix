package com.enablix.app.report.activity.metric.metricesimpl;

import java.util.Date;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.enablix.commons.constants.AppConstants.MetricTypes;
import com.enablix.commons.util.date.DateUtil;
import com.enablix.core.domain.report.activitymetric.MetricStats;

@Component
public class ContentUpdateMetricImpl extends SimpleMetric {

	private final MetricTypes activityCode = MetricTypes.CONTENT_UPDATES;

	private final String collectionName = "ebx_activity_audit";

	@Override
	public MetricStats calculate(Date startDate, Date endDate) {

		startDate = DateUtil.getStartOfDay(startDate);
		endDate = DateUtil.getEndOfDay(endDate);
		
		Criteria criteria = Criteria.where("createdAt").gte(startDate).lte(endDate);
		criteria = criteria.and("activity.activityType").is("CONTENT_UPDATE");
		return calculateSimpleMetric(startDate, endDate, 
				collectionName, activityCode, criteria);
	}

	@Override
	public MetricTypes getActivityCode() {
		return activityCode;
	}

}
