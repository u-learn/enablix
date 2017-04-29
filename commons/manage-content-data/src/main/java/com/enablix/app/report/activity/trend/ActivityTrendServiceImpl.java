package com.enablix.app.report.activity.trend;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.report.activity.metric.ActivityMetricRepository;
import com.enablix.commons.constants.AppConstants;
import com.enablix.core.domain.report.activitymetric.ActivityMetric;
import com.enablix.core.domain.report.activitymetric.MetricStats;

@Component
public class ActivityTrendServiceImpl implements ActivityTrendService{

	@Autowired
	private ActivityMetricRepository activityMetric;
	
	@Override
	public List<Map<String, Integer>> getActivityTrends(String date, List<String> activityMetrices) throws ParseException {
		
		Date activityMetricDate = new SimpleDateFormat("dd-MMM-yy").parse(date);
	
		//List<ActivityMetric> activityMetrics = activityMetric.findByAsOfDateGreaterThan(activityMetrices, activityMetricDate);
		
		//List<Map<String, Integer>> activityTrend = transformMetricToTrend(activityMetrics);
		
		return null;
	}

	private List<Map<String, Integer>> transformMetricToTrend(List<ActivityMetric> activityMetrics) {
		List<Map<String, Integer>> activityTrend = new ArrayList<Map<String, Integer>>(activityMetrics.size());
		Map<String, Integer> activityTrendMap;
		Integer counter = 1;
		for(ActivityMetric activityMetric : activityMetrics){
			
			activityTrendMap = new HashMap<String, Integer>();
			
			activityTrendMap.put(AppConstants.ACTIVITY_TREND_KEY, counter);
			List<MetricStats> metricStats =	activityMetric.getMetricStats();
			Collections.sort(metricStats);
			for(MetricStats metricStat : metricStats){
				//activityTrendMap.put(metricStat.getMetricName(), metricStat.getMetricValue());
			}
			
			activityTrend.add(activityTrendMap);
			counter++;
		}
		return activityTrend;
	}
	
}