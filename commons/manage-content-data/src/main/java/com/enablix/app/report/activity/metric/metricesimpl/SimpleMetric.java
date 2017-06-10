package com.enablix.app.report.activity.metric.metricesimpl;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.enablix.app.report.activity.metric.MetricStatsCalculator;
import com.enablix.app.report.util.TrendUtil;
import com.enablix.commons.constants.AppConstants.MetricTypes;
import com.enablix.core.domain.activity.ActivityAudit;
import com.enablix.core.domain.report.activitymetric.MetricStats;
import com.enablix.core.domain.report.activitymetric.TrendAggMetric;

public abstract class SimpleMetric implements MetricStatsCalculator {

	@Autowired
	protected MongoTemplate mongoTemplate;

	private Query query;
	
	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}
	
	public Query getQuery() {
		return query;
	}
	
	public MetricStats calculateSimpleMetricStats(Date startDate, Date endDate, String collectionName, 
			MetricTypes activityCode, Criteria criteria) {

		MetricStats metricStats=null;
		Query query = getQuery();

		query = Query.query(criteria);

		Long count = mongoTemplate.count(query, collectionName);

		metricStats = new MetricStats(activityCode.getCode(),activityCode.getValue(), count);

		return metricStats;
	}
	
	public List<MetricStats> calculateSimpleMetricDailyTrend(Criteria criteria, MetricTypes activityCode, Date startDate, Date endDate) {
		Aggregation aggregation = newAggregation(
				match(Criteria.where("createdAt").gte(startDate)), 
				match(Criteria.where("createdAt").lte(endDate)), 
				match(criteria), 
				group("dateDimension.dayOfYear", "dateDimension.year"
						).count().as("sum"),
				Aggregation.project(Fields.from(
						Fields.field("dayOfYear", "$_id.dayOfYear"),
						Fields.field("year", "$_id.year"),
						Fields.field("value", "$sum"))
						),
				sort(Direction.ASC , "year", "dayOfYear"));

		AggregationResults<TrendAggMetric> groupResults = mongoTemplate.aggregate(aggregation, 
				ActivityAudit.class, TrendAggMetric.class);
		List<TrendAggMetric> groupMetrics= groupResults.getMappedResults();
		return TrendUtil.updateDailyMissingValues(groupMetrics, startDate, endDate, activityCode.getCode(), activityCode.getValue());
	}
	
	public List<MetricStats> calculateSimpleMetricWeeklyTrend(Criteria criteria, MetricTypes activityCode, Date startDate, Date endDate) {
		Aggregation aggregation = newAggregation(
				match(Criteria.where("createdAt").gte(startDate)), 
				match(Criteria.where("createdAt").lte(endDate)), 
				match(criteria), 
				group("dateDimension.weekOfYear",
						"dateDimension.year"
						).count().as("sum"),
				Aggregation.project(Fields.from(
						Fields.field("weekOfYear", "$_id.weekOfYear"),
						Fields.field("year", "$_id.year"),
						Fields.field("value", "$sum"))
						),
				sort(Direction.ASC , "year", "weekOfYear"));

		AggregationResults<TrendAggMetric> groupResults = mongoTemplate.aggregate(aggregation, 
				ActivityAudit.class, TrendAggMetric.class);
		List<TrendAggMetric> groupMetrics= groupResults.getMappedResults();
		return TrendUtil.updateWeeklyMissingValues(groupMetrics, startDate, endDate, activityCode.getCode(), activityCode.getValue());
	}
	
	public List<MetricStats> calculateSimpleMetricMonthlyTrend(Criteria criteria, MetricTypes activityCode, Date startDate, Date endDate) {
		Aggregation aggregation = newAggregation(
				match(Criteria.where("createdAt").gte(startDate)), 
				match(Criteria.where("createdAt").lte(endDate)), 
				match(criteria), 
				group("dateDimension.monthOfYear",
						"dateDimension.year"
						).count().as("sum"),
				Aggregation.project(Fields.from(
						Fields.field("monthOfYear", "$_id.monthOfYear"),
						Fields.field("year", "$_id.year"),
						Fields.field("value", "$sum"))
						),
				sort(Direction.ASC , "year", "monthOfYear"));

		AggregationResults<TrendAggMetric> groupResults = mongoTemplate.aggregate(aggregation, 
				ActivityAudit.class, TrendAggMetric.class);
		List<TrendAggMetric> groupMetrics= groupResults.getMappedResults();
		return TrendUtil.updateMonthlyMissingValues(groupMetrics, startDate, endDate, activityCode.getCode(), activityCode.getValue());
	}
}