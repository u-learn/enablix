package com.enablix.app.report.activity.metric.utils;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.enablix.commons.constants.AppConstants.MetricTypes;
import com.enablix.commons.util.date.DateUtil;
import com.enablix.core.domain.report.activitymetric.ActivityMetric;
import com.enablix.core.domain.report.activitymetric.MetricStats;

@Component
public class MetricAggService {

	@Autowired
	private MongoTemplate mongoTemplate;

	public MetricStats getSimpleAggStats(Date startDate, Date endDate, MetricTypes activityCode) {
		
		startDate = DateUtil.getStartOfDay(startDate);
		endDate = DateUtil.getEndOfDay(endDate);

		Aggregation aggregation = newAggregation(
				unwind("metricStats"),
				match(Criteria.where("metricStats.metricName").is(activityCode.getValue())),
				match(Criteria.where("asOfDate").gte(startDate)), 
				match(Criteria.where("asOfDate").lte(endDate)), 
				group("$metricStats.metricName").sum("$metricStats.metricValue").as("sum"),
				Aggregation.project(Fields.from(
						Fields.field("metricName", "$_id"),
						Fields.field("metricValue", "$sum"))));

		AggregationResults<MetricStats> groupResults = mongoTemplate.aggregate(aggregation, ActivityMetric.class,
				MetricStats.class);
		List<MetricStats> groupMetrics= groupResults.getMappedResults();
		if (groupMetrics == null || groupMetrics.size()==0){
			long metricValue = 0 ;
			MetricStats metricStats = new MetricStats(activityCode.getCode(), activityCode.getValue(), metricValue);
			return metricStats;
		}
		return groupResults.getMappedResults().get(0);
	}
}
