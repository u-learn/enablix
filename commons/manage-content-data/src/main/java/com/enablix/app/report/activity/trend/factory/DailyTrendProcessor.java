package com.enablix.app.report.activity.trend.factory;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.enablix.core.domain.report.activitymetric.ActivityMetric;
import com.enablix.core.domain.report.activitymetric.MetricStats;

@Component
public class DailyTrendProcessor implements ActivityTrendProcessor {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public List<MetricStats> getTrendData(Date startDate, Date endDate, List<String> filteredMetric) {
		Aggregation aggregation = newAggregation(
				unwind("metricStats"),
				match(Criteria.where("asOfDate").gte(startDate)), 
				match(Criteria.where("asOfDate").lte(endDate)), 
				match(Criteria.where("metricStats.metricCode").in(filteredMetric)), 
				group("dateDimension.dayOfYear", "dateDimension.weekOfMonth","metricStats.metricName","metricStats.metricCode",
					  "dateDimension.monthOfYear", "dateDimension.year"
						).sum("$metricStats.metricValue").as("sum"),
				Aggregation.project(Fields.from(
						Fields.field("metricName", "$_id.metricName"),
						Fields.field("dayOfYear", "$_id.dayOfYear"),
						Fields.field("weekOfMonth", "$_id.weekOfMonth"),
						Fields.field("monthOfYear", "$_id.monthOfYear"),
						Fields.field("year", "$_id.year"),
						Fields.field("metricCode", "$_id.metricCode"),
						Fields.field("metricValue", "$sum"))
						),
				sort(Direction.ASC , "year", "monthOfYear", "weekOfMonth", "dayOfYear", "metricName"));

		AggregationResults<MetricStats> groupResults = mongoTemplate.aggregate(aggregation, 
				ActivityMetric.class, MetricStats.class);
		List<MetricStats> groupMetrics= groupResults.getMappedResults();
		return groupMetrics;
	}
}