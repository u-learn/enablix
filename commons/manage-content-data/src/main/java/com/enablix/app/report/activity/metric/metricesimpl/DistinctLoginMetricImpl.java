package com.enablix.app.report.activity.metric.metricesimpl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.enablix.commons.constants.AppConstants.MetricTypes;
import com.enablix.commons.util.date.DateUtil;
import com.enablix.core.domain.report.activitymetric.MetricStats;

@Component
public class DistinctLoginMetricImpl extends DynamicMetric {

	private final MetricTypes activityCode = MetricTypes.DISTINCT_LOGIN;
	
	private final String collectionName = "ebx_activity_audit";
	
	@Override
	public MetricTypes getActivityCode() {
		return activityCode;
	}

	@Override
	public MetricStats calculate(Date executionDate) {
		//Returning null as these metrices will be calculated at runtime
		return null;
	}

	@Override
	public MetricStats getAggStats(Date startDate) {
		
		Date backDate = DateUtil.getEndOfDay(DateUtils.addDays(new Date(),-1));
		
		MetricStats metricStats=null;

		startDate = DateUtil.getStartOfDay(startDate);
		
		criteria = Criteria.where("createdAt").gte(startDate).lte(backDate);
		
		criteria = criteria.and("activity.accountActivityType").is("LOGIN");
		
		query = Query.query(criteria);

		List distinctUserLst = mongoTemplate.getCollection(collectionName).distinct("actor.userId", query.getQueryObject());
		Long count;
		if(distinctUserLst == null || distinctUserLst.size() == 0){
			count = Long.valueOf(0);
		}
		else {
			count = Long.valueOf(distinctUserLst.size());
		}
		metricStats = new MetricStats(activityCode.getCode(),activityCode.getValue(), count);

		return metricStats;
	}

}
