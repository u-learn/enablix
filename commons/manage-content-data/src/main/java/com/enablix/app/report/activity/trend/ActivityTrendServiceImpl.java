package com.enablix.app.report.activity.trend;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.report.activity.metric.MetricStatsCalculator;
import com.enablix.app.report.activity.metric.MetricStatsCalculatorFactory;
import com.enablix.commons.constants.AppConstants;
import com.enablix.commons.constants.AppConstants.ActivityTrend;
import com.enablix.commons.util.date.DateUtil;
import com.enablix.core.domain.report.activitymetric.ActivityMetricType;
import com.enablix.core.domain.report.activitymetric.MetricStats;

@Component
public class ActivityTrendServiceImpl implements ActivityTrendService {

	@Autowired
	private MetricStatsCalculatorFactory metricCalcFactory;

	private static final Logger LOGGER = LoggerFactory.getLogger(ActivityTrendServiceImpl.class);

	public List<ActivityMetricType> getActivityMetricConfig() {
		return ActivityMetricType.getAllTypes();
	}

	@Override
	public List<Map<String, Long>> getActivityTrends(Date startDate, Date endDate, 
			List<String> filterActivityMetrices, String trendType) throws ParseException {

		Date trendStartDate = DateUtil.getStartOfDay(startDate);
		Date trendEndDate = DateUtil.getEndOfDay(endDate);

		Map<String, List<MetricStats>> trendData = new HashMap<String, List<MetricStats>>();
		
		filterActivityMetrices.forEach((metricCode) -> {
			
			MetricStatsCalculator metricStatCalc = metricCalcFactory.getMetricCalculator(metricCode);
			
			if (metricStatCalc != null ){
				
				List<MetricStats> metricStat = getTrendData(metricStatCalc, trendType, trendStartDate, trendEndDate);
				
				if (metricStat == null) {
					LOGGER.debug("Metric calculator returned null value for [{}] metric", metricCode);
				} else {
					trendData.put(metricCode, metricStat);
				}
				
			} else {
				LOGGER.error("No metric calculator found for metric [{}]", metricCode);
			}
			
		});
		
		List<Map<String, Long>> activityTrend = transformDataToUIFormat(trendData);
		
		return activityTrend;
	}

	private List<MetricStats> getTrendData(MetricStatsCalculator metricStatCalc, 
			String trendType, Date startDate, Date endDate) {
		
		List<MetricStats> metricStat = null;
		
		if (ActivityTrend.DAILY.getValue().equalsIgnoreCase(trendType)) {
			
			metricStat = metricStatCalc.calculateDailyTrend(startDate, endDate);
			
		} else if (ActivityTrend.WEEKLY.getValue().equalsIgnoreCase(trendType)) {
			
			metricStat = metricStatCalc.calculateWeeklyTrend(startDate, endDate);
			
		} else if (ActivityTrend.MONTHLY.getValue().equalsIgnoreCase(trendType)) {
			
			metricStat = metricStatCalc.calculateMonthlyTrend(startDate, endDate);
		}
		
		return metricStat;
	}

	private List<Map<String, Long>> transformDataToUIFormat(Map<String, List<MetricStats>> trendData) {

		List<Map<String, Long>> activityTrend = new ArrayList<Map<String, Long>>(trendData.size());
		
		for (Entry<String, List<MetricStats>> trendMetric : trendData.entrySet()) {
			
			List<MetricStats> metricStats = trendMetric.getValue();
			int metricCounter = 0;
			
			for (MetricStats metricStat : metricStats) {
				
				int size = activityTrend.size();
				
				Map<String, Long> trendMap;
				
				if (size <= metricCounter) {
					trendMap = new HashMap<String, Long>();
					activityTrend.add(trendMap);	
				} else{
					trendMap = activityTrend.get(metricCounter);
				}
				
				metricCounter++;
				
				trendMap.put(metricStat.getMetricCode(), metricStat.getMetricValue());		
				trendMap.put(AppConstants.ACTIVITY_TREND_KEY, new Long(metricCounter));
				trendMap.put("startDate", metricStat.getMetricDate().getTime());
			}
		}
		
		return activityTrend;
	}
	
}