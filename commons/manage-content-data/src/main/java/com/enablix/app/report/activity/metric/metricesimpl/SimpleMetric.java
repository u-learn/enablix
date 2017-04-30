package com.enablix.app.report.activity.metric.metricesimpl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.enablix.app.report.activity.metric.MetricStatsCalculator;
import com.enablix.app.report.activity.metric.utils.MetricAggService;
import com.enablix.commons.constants.AppConstants.MetricTypes;
import com.enablix.core.domain.report.activitymetric.MetricStats;

public abstract class SimpleMetric implements MetricStatsCalculator {

	@Autowired
	private MetricAggService metricAggService;
	
	@Autowired
	private MongoTemplate mongoTemplate;

	private Query query;
	
	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}
	
	public Query getQuery() {
		return query;
	}
	
	public MetricStats calculateSimpleMetric(Date startDate, Date endDate, String collectionName, 
			MetricTypes activityCode, Criteria criteria) {

		MetricStats metricStats=null;
		Query query = getQuery();

		query = Query.query(criteria);

		Long count = mongoTemplate.count(query, collectionName);

		metricStats = new MetricStats(activityCode.getCode(),activityCode.getValue(), count);

		return metricStats;
	}

	@Override
	public MetricStats getAggStats(Date startDate, Date endDate) {
		return metricAggService.getSimpleAggStats(startDate, endDate, getActivityCode());
	}

	
}