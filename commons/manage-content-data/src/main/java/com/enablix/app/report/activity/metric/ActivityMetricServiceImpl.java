package com.enablix.app.report.activity.metric;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ScriptOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.script.ExecutableMongoScript;
import org.springframework.stereotype.Component;

import com.enablix.core.domain.report.activitymetric.ActivityMetric;
import com.enablix.core.domain.report.activitymetric.ActivityMetricConfig;
import com.enablix.core.domain.report.activitymetric.ReportStats;
import com.enablix.core.mongo.dao.BaseTenantDao;

@Component
public class ActivityMetricServiceImpl extends BaseTenantDao implements ActivityMetricService {


	@Autowired
	ActivityMetricConfigRepository activityMetricRepo;

	@Override
	public List<ActivityMetricConfig> getActivityMetricConfig() {
		List<ActivityMetricConfig> activityMetrices = activityMetricRepo.findAll();
		return activityMetrices;
	}

	@Override
	public ReportStats executeActivityMetrices(ActivityMetricConfig activityMetric) {
		MongoTemplate mongoTemplate = getMongoTemplate();
		ReportStats reportStat;

		ScriptOperations scriptOps = mongoTemplate.scriptOps();
		ExecutableMongoScript echoScript = new ExecutableMongoScript(activityMetric.getMetricQueryFn());
		Integer value =  ((Double)scriptOps.execute(echoScript, "directly execute script")).intValue();;
		reportStat =  new ReportStats(activityMetric.getMetricName(),value);
		return reportStat;
	}

	@Override
	public List<ReportStats> getAggregatedValues(Date date) {
		
		MongoTemplate mongoTemplate = getMongoTemplate();
		Aggregation aggregation = newAggregation(
				match(Criteria.where("asOfDate").gte(date)),
				unwind("reportStats"),
				group("reportStats._id").sum("reportStats.reportValue").as("reportValue")
				);

		AggregationResults<ReportStats> groupResults = mongoTemplate.aggregate(aggregation, ActivityMetric.class
				, ReportStats.class);

		List<ReportStats> activityReport = groupResults.getMappedResults();

		return activityReport;
	}
}
