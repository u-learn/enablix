package com.enablix.app.report.activity.trend;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.report.activity.trend.factory.ActivityTrendFactory;
import com.enablix.app.report.activity.trend.factory.ActivityTrendProcessor;
import com.enablix.commons.constants.AppConstants;
import com.enablix.commons.constants.AppConstants.MetricTypes;
import com.enablix.commons.util.date.DateUtil;
import com.enablix.core.domain.report.activitymetric.MetricStats;

@Component
public class ActivityTrendServiceImpl implements ActivityTrendService {

	@Autowired
	ActivityTrendFactory activityTrendFactory;

	@Override
	public List<Map<String, Long>> getActivityTrends(Date startDate, Date endDate, List<String> filterActivityMetrices, String trendType) throws ParseException {

		startDate = DateUtil.getStartOfDay(startDate);
		endDate = DateUtil.getStartOfDay(endDate);

		ActivityTrendProcessor trendProc = activityTrendFactory.getTrendProcessor(trendType);
		List<MetricStats> metricStats = trendProc.getTrendData(startDate, endDate);
		List<Map<String, Long>> activityTrend = transformDataToUIFormat(metricStats, filterActivityMetrices);

		return activityTrend;
	}

	private List<Map<String, Long>> transformDataToUIFormat(List<MetricStats> metricStats, List<String> filterActivityMetrices) {

		List<Map<String, Long>> activityTrend = new ArrayList<Map<String, Long>>(metricStats.size());
		Map<String, Long> activityTrendMap;
		long counter = 0;
		long time = 1;
		long metricLength = MetricTypes.values().length;
		activityTrendMap = new HashMap<String, Long>();
		activityTrend.add(activityTrendMap);
		for(MetricStats metricStat : metricStats) {
			if(filterActivityMetrices.contains(metricStat.getMetricCode())) {
				if(counter == metricLength){
					counter = 0;
					activityTrendMap = new HashMap<String, Long>();
					activityTrend.add(activityTrendMap);
					time++;
				}
				activityTrendMap.put(AppConstants.ACTIVITY_TREND_KEY, time);
				activityTrendMap.put(metricStat.getMetricCode(), metricStat.getMetricValue());
			}
			counter++;
		}
		return activityTrend;
	}
}