package com.enablix.app.report.activity.metric;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.core.domain.report.activitymetric.ActivityMetricType;
import com.enablix.core.domain.report.activitymetric.MetricStats;
import com.enablix.core.mongo.search.service.SearchRequest;

@Component
public class ActivityMetricServiceImpl implements ActivityMetricService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ActivityMetricServiceImpl.class);
	
	@Autowired
	private MetricStatsCalculatorFactory metricCalcFactory;
	
	@Override
	public List<MetricStats> getAggregatedValues(Date startDate, Date endDate, SearchRequest userFilter) {
		
		List<MetricStats> metricStats = new ArrayList<MetricStats>();
		
		for (ActivityMetricType activityMetricConfig : ActivityMetricType.getAllTypes()) {
			MetricStatsCalculator metricStatCalc = metricCalcFactory.getMetricCalculator(activityMetricConfig.getMetricCode());
			if( metricStatCalc == null ){
				LOGGER.debug("No Implementation Found for the Activity [{}]", activityMetricConfig);
				continue;
			}
			MetricStats metricStat = metricStatCalc.calculateStats(startDate, endDate, userFilter);
			metricStats.add(metricStat);
		}
		
		return metricStats;
		
	}

}
