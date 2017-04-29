package com.enablix.app.report.activity.metric.metricesimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.enablix.app.report.activity.metric.MetricStatsCalculator;

public abstract class DynamicMetric implements MetricStatsCalculator{
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
}