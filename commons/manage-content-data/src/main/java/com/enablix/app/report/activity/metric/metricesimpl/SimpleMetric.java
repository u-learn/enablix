package com.enablix.app.report.activity.metric.metricesimpl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.enablix.app.report.activity.metric.MetricStatsCalculator;
import com.enablix.commons.constants.AppConstants.MetricTypes;
import com.enablix.commons.util.date.DateUtil;
import com.enablix.core.domain.report.activitymetric.MetricStats;

public abstract class SimpleMetric implements MetricStatsCalculator {

	@Autowired
	private MongoTemplate mongoTemplate;

	private Criteria criteria = new Criteria();;

	private Query query;
	
	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}
	
	public Criteria getCriteria() {
		return criteria;
	}
	
	public Query getQuery() {
		return query;
	}
	
	public MetricStats calculateSimpleMetric(Date executionDate, String collectionName, 
			MetricTypes activityCode, HashMap<String, String> fieldValue) {

		MetricStats metricStats=null;
		Query query = getQuery();

		Date startDate = DateUtil.getStartOfDay(executionDate);
		Date endDate = DateUtil.getEndOfDay(executionDate);

		
		criteria = Criteria.where("createdAt").gte(startDate).lte(endDate);
		
		for(Entry<String, String> field : fieldValue.entrySet()){
			criteria = criteria.and(field.getKey()).is(field.getValue());
		}
		
		query = Query.query(criteria);

		Long count = mongoTemplate.count(query, collectionName);

		metricStats = new MetricStats(activityCode.getCode(),activityCode.getValue(), count);

		return metricStats;
	}
	
}