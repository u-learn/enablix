package com.enablix.app.report.activity.metric.metricesimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.enablix.app.report.activity.metric.MetricStatsCalculator;

public abstract class DynamicMetric implements MetricStatsCalculator {
	
	@Autowired
	protected MongoTemplate mongoTemplate;

	protected Criteria criteria = new Criteria();;

	protected Query query;
	
}