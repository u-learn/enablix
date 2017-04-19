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
import com.enablix.core.domain.report.activitymetric.MetricStats;

@Component
public class ActivityMetricServiceImpl  implements ActivityMetricService {

	@Autowired
	private ActivityMetricConfigRepository activityMetricRepo;

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public List<ActivityMetricConfig> getActivityMetricConfig() {
		List<ActivityMetricConfig> activityMetrices = activityMetricRepo.findAll();
		return activityMetrices;
	}

	@Override
	public MetricStats executeActivityMetrices(ActivityMetricConfig activityMetric) {
		MetricStats reportStat;

		ScriptOperations scriptOps = mongoTemplate.scriptOps();
		ExecutableMongoScript echoScript = new ExecutableMongoScript(activityMetric.getMetricQueryFn());
		Integer value = ((Double) scriptOps.execute(echoScript)).intValue();
		
		reportStat = new MetricStats(activityMetric.getMetricName(), value);
		return reportStat;
	}

	@Override
	public List<MetricStats> getAggregatedValues(Date date) {

		Aggregation aggregation = newAggregation(match(Criteria.where("asOfDate").gte(date)), unwind("metricStats"),
				group("metricStats._id").sum("metricStats.metricValue").as("metricValue"));

		AggregationResults<MetricStats> groupResults = mongoTemplate.aggregate(aggregation, ActivityMetric.class,
				MetricStats.class);

		List<MetricStats> activityReport = groupResults.getMappedResults();

		return activityReport;
	}
}
