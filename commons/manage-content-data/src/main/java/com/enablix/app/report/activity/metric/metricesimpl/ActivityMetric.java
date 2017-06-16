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
import com.enablix.commons.constants.AppConstants.MetricType;
import com.enablix.commons.util.date.DateUtil;
import com.enablix.core.domain.activity.ActivityAudit;
import com.enablix.core.domain.report.activitymetric.MetricStats;
import com.enablix.core.domain.report.activitymetric.TrendAggMetric;

public abstract class ActivityMetric implements MetricStatsCalculator {

	@Autowired
	protected MongoTemplate mongoTemplate;

	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}
	
	protected abstract Criteria baseCriteria();
	
	@Override
	public MetricStats calculateStats(Date startDate, Date endDate) {
		
		startDate = DateUtil.getStartOfDay(startDate);
		endDate = DateUtil.getEndOfDay(endDate);
		
		Criteria criteria = Criteria.where("createdAt").gte(startDate).lte(endDate);
		criteria = criteria.andOperator(baseCriteria());
		
		return calculateMetricStats(startDate, endDate, metricType(), criteria);
	}
	
	public MetricStats calculateMetricStats(Date startDate, Date endDate, MetricType activityCode, Criteria criteria) {

		MetricStats metricStats = null;
		
		Query query = Query.query(criteria);
		Long count = mongoTemplate.count(query, ActivityAudit.class);

		metricStats = new MetricStats(activityCode.getCode(), activityCode.getValue(), count, startDate);

		return metricStats;
	}
	

	@Override
	public List<MetricStats> calculateDailyTrend(Date startDate, Date endDate) {
		
		Aggregation aggregation = dailyAggregation(startDate, endDate);

		AggregationResults<TrendAggMetric> groupResults = 
				mongoTemplate.aggregate(aggregation, ActivityAudit.class, TrendAggMetric.class);
		
		List<TrendAggMetric> groupMetrics= groupResults.getMappedResults();
		
		return TrendUtil.updateDailyMissingValues(groupMetrics, 
				startDate, endDate, metricType().getCode(), metricType().getValue());
	}

	protected Aggregation dailyAggregation(Date startDate, Date endDate) {
		
		Aggregation aggregation = newAggregation(
				match(Criteria.where("createdAt").gte(startDate).lte(endDate)), 
				match(baseCriteria()), 
				group("dateDimension.dayOfYear", "dateDimension.year").count().as("sum"),
				Aggregation.project(Fields.from(
						Fields.field("dayOfYear", "$_id.dayOfYear"),
						Fields.field("year", "$_id.year"),
						Fields.field("value", "$sum"))),
				sort(Direction.ASC , "year", "dayOfYear"));
		
		return aggregation;
	}
	
	@Override
	public List<MetricStats> calculateWeeklyTrend(Date startDate, Date endDate) {
		
		Aggregation aggregation = weeklyAggregate(startDate, endDate);

		AggregationResults<TrendAggMetric> groupResults = 
				mongoTemplate.aggregate(aggregation, ActivityAudit.class, TrendAggMetric.class);
		
		List<TrendAggMetric> groupMetrics= groupResults.getMappedResults();
		
		return TrendUtil.updateWeeklyMissingValues(groupMetrics, 
				startDate, endDate, metricType().getCode(), metricType().getValue());
	}

	protected Aggregation weeklyAggregate(Date startDate, Date endDate) {
		
		Aggregation aggregation = newAggregation(
				match(Criteria.where("createdAt").gte(startDate).lte(endDate)), 
				match(baseCriteria()), 
				group("dateDimension.weekOfYear", "dateDimension.year").count().as("sum"),
				Aggregation.project(Fields.from(
						Fields.field("weekOfYear", "$_id.weekOfYear"),
						Fields.field("year", "$_id.year"),
						Fields.field("value", "$sum"))),
				sort(Direction.ASC , "year", "weekOfYear"));
		
		return aggregation;
	}
	
	@Override
	public List<MetricStats> calculateMonthlyTrend(Date startDate, Date endDate) {
		
		Aggregation aggregation = monthlyAggregate(startDate, endDate);

		AggregationResults<TrendAggMetric> groupResults = 
				mongoTemplate.aggregate(aggregation, ActivityAudit.class, TrendAggMetric.class);
		
		List<TrendAggMetric> groupMetrics= groupResults.getMappedResults();
		
		return TrendUtil.updateMonthlyMissingValues(groupMetrics, startDate, 
				endDate, metricType().getCode(), metricType().getValue());
	}

	protected Aggregation monthlyAggregate(Date startDate, Date endDate) {
		
		Aggregation aggregation = newAggregation(
				match(Criteria.where("createdAt").gte(startDate).lte(endDate)), 
				match(baseCriteria()), 
				group("dateDimension.monthOfYear", "dateDimension.year").count().as("sum"),
				Aggregation.project(Fields.from(
						Fields.field("monthOfYear", "$_id.monthOfYear"),
						Fields.field("year", "$_id.year"),
						Fields.field("value", "$sum"))),
				sort(Direction.ASC , "year", "monthOfYear"));
		
		return aggregation;
	}
}