package com.enablix.app.report.activity.metric.metricesimpl;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.enablix.commons.constants.AppConstants.MetricType;
import com.enablix.commons.util.date.DateUtil;
import com.enablix.core.domain.report.activitymetric.MetricStats;

@Component
public class DistinctLoginMetricImpl extends ActivityMetric {

	private Criteria noOfLoginsCriteria = Criteria.where("activity.accountActivityType").is("LOGIN");

	private final String collectionName = "ebx_activity_audit";

	@Override
	public MetricType metricType() {
		return MetricType.DISTINCT_LOGIN;
	}

	@Override
	public MetricStats calculateStats(Date startDate, Date endDate) {

		MetricStats metricStats = null;

		startDate = DateUtil.getStartOfDay(startDate);
		endDate = DateUtil.getEndOfDay(endDate);

		Criteria criteria = Criteria.where("createdAt").gte(startDate).lte(endDate);
		criteria = criteria.andOperator(baseCriteria());

		Query query = Query.query(criteria);

		List<?> distinctUserLst = 
				mongoTemplate.getCollection(collectionName).distinct("actor.userId",
				query.getQueryObject());

		Long count = 0L;
		if (distinctUserLst != null) {
			count = Long.valueOf(distinctUserLst.size());
		}
		
		metricStats = new MetricStats(metricType().getCode(), metricType().getValue(), count, startDate);

		return metricStats;

	}

	protected Aggregation dailyAggregation(Date startDate, Date endDate) {
		
		Aggregation aggregation = newAggregation(
				match(Criteria.where("createdAt").gte(startDate).lte(endDate)
							  .and("activity.accountActivityType").is("LOGIN")),
				group("dateDimension.dayOfYear", "dateDimension.year", "actor.userId").count().as("sum"),
				group("$_id.dayOfYear", "$_id.year").count().as("sum").count().as("sum"),
				Aggregation.project(Fields.from(
						Fields.field("dayOfYear", "$_id.dayOfYear"),
						Fields.field("year", "$_id.year"),
						Fields.field("value", "$sum"))),
				sort(Direction.ASC , "year", "dayOfYear"));
		
		return aggregation;
	}
	
	protected Aggregation weeklyAggregate(Date startDate, Date endDate) {
		
		Aggregation aggregation = newAggregation(
				match(Criteria.where("createdAt").gte(startDate).lte(endDate)
						 	  .and("activity.accountActivityType").is("LOGIN")), 
				group("dateDimension.weekOfYear", "dateDimension.year", "actor.userId").count().as("sum"),
				group("$_id.weekOfYear", "$_id.year").count().as("sum").count().as("sum"),
				Aggregation.project(Fields.from(
						Fields.field("weekOfYear", "$_id.weekOfYear"),
						Fields.field("year", "$_id.year"),
						Fields.field("value", "$sum"))),
				sort(Direction.ASC , "year", "weekOfYear"));
		
		return aggregation;
	}
	
	protected Aggregation monthlyAggregate(Date startDate, Date endDate) {
		
		Aggregation aggregation = newAggregation(
				match(Criteria.where("createdAt").gte(startDate).lte(endDate)
							  .and("activity.accountActivityType").is("LOGIN")), 
				group("dateDimension.monthOfYear", "dateDimension.year", "actor.userId").count().as("sum"),
				group("$_id.monthOfYear", "$_id.year").count().as("sum").count().as("sum"),
				Aggregation.project(Fields.from(
						Fields.field("monthOfYear", "$_id.monthOfYear"),
						Fields.field("year", "$_id.year"),
						Fields.field("value", "$sum"))),
				sort(Direction.ASC , "year", "monthOfYear"));
		
		return aggregation;
	}

	@Override
	protected Criteria baseCriteria() {
		return noOfLoginsCriteria;
	}
	
}
