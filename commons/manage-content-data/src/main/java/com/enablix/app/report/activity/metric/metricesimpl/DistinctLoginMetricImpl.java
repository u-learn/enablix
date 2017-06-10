package com.enablix.app.report.activity.metric.metricesimpl;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.enablix.commons.constants.AppConstants.MetricTypes;
import com.enablix.commons.util.date.DateUtil;
import com.enablix.core.domain.report.activitymetric.MetricStats;

@Component
public class DistinctLoginMetricImpl extends ComplexMetric {

	private final MetricTypes activityCode = MetricTypes.DISTINCT_LOGIN;

	private final String collectionName = "ebx_activity_audit";

	@Override
	public MetricTypes getActivityCode() {
		return activityCode;
	}

	@Override
	public MetricStats calculateStats(Date startDate, Date endDate) {

		MetricStats metricStats = null;

		startDate = DateUtil.getStartOfDay(startDate);
		endDate = DateUtil.getEndOfDay(endDate);

		criteria = Criteria.where("createdAt").gte(startDate).lte(endDate);

		criteria = criteria.and("activity.accountActivityType").is("LOGIN");

		query = Query.query(criteria);

		List<?> distinctUserLst = mongoTemplate.getCollection(collectionName).distinct("actor.userId",
				query.getQueryObject());

		Long count;
		if (distinctUserLst == null || distinctUserLst.size() == 0) {
			count = Long.valueOf(0);
		} else {
			count = Long.valueOf(distinctUserLst.size());
		}
		metricStats = new MetricStats(activityCode.getCode(), activityCode.getValue(), count);

		return metricStats;

	}

	@Override
	public List<MetricStats> calculateDailyTrend(Date startDate, Date endDate) {
		return null;
	}

	@Override
	public List<MetricStats> calculateWeeklyTrend(Date startDate, Date endDate) {
		return null;
	}
	
	@Override
	public List<MetricStats> calculateMonthlyTrend(Date startDate, Date endDate) {
		return null;
	}
	
}
