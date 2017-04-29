package com.enablix.app.report.activity.metric;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.core.domain.report.activitymetric.ActivityMetricConfig;
import com.enablix.core.domain.report.activitymetric.MetricStats;

@Component
public class ActivityMetricServiceImpl  implements ActivityMetricService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ActivityMetricServiceImpl.class);
	
	@Autowired
	private ActivityMetricConfigRepository activityMetricRepo;

	@Autowired
	MetricStatsCalculatorFactory metricCalcFactory;
	
	@Override
	public List<ActivityMetricConfig> getActivityMetricConfig() {
		List<ActivityMetricConfig> activityMetrices = activityMetricRepo.findAll();
		return activityMetrices;
	}

	@Override
	public MetricStats executeActivityMetrices(ActivityMetricConfig activityMetric, Date executionDate) {
		MetricStats reportStat=null;
		MetricStatsCalculator metricStatCalc = metricCalcFactory.getMetricCalculator(activityMetric.getMetricCode());
		if(metricStatCalc == null ){
			return null;
		}
		reportStat = metricStatCalc.calculate(executionDate);
		return reportStat;
	}

	@Override
	public List<MetricStats> getAggregatedValues(Date date) throws ParseException {
		List<MetricStats> metricStats = new ArrayList<MetricStats>();
		List<ActivityMetricConfig> activityMetrics = getActivityMetricConfig();
		
		for (ActivityMetricConfig activityMetricConfig : activityMetrics) {
			MetricStatsCalculator metricStatCalc = metricCalcFactory.getMetricCalculator(activityMetricConfig.getMetricCode());
			if( metricStatCalc == null ){
				LOGGER.debug(" No Implementation Found for the Activity "+activityMetricConfig);
				continue;
			}
			MetricStats metricStat = metricStatCalc.getAggStats(date);
			metricStats.add(metricStat);
		}
		
		return metricStats;
		
	}
}
