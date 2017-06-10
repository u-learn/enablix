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

import com.enablix.app.report.activity.metric.ActivityMetricConfigRepository;
import com.enablix.app.report.activity.metric.MetricStatsCalculator;
import com.enablix.app.report.activity.metric.MetricStatsCalculatorFactory;
import com.enablix.commons.constants.AppConstants;
import com.enablix.commons.constants.AppConstants.ActivityTrend;
import com.enablix.commons.util.date.DateUtil;
import com.enablix.core.domain.report.activitymetric.ActivityMetricConfig;
import com.enablix.core.domain.report.activitymetric.MetricStats;

@Component
public class ActivityTrendServiceImpl implements ActivityTrendService {

	@Autowired
	private ActivityMetricConfigRepository activityMetricRepo;

	@Autowired
	private MetricStatsCalculatorFactory metricCalcFactory;

	private static final Logger LOGGER = LoggerFactory.getLogger(ActivityTrendServiceImpl.class);

	@Override
	public List<ActivityMetricConfig> getActivityMetricConfig() {
		List<ActivityMetricConfig> activityMetrices = activityMetricRepo.findAll();
		return activityMetrices;
	}

	@Override
	public List<Map<String, Long>> getActivityTrends(Date startDate, Date endDate, 
			List<String> filterActivityMetrices, String trendType) throws ParseException {

		startDate = DateUtil.getStartOfDay(startDate);
		endDate = DateUtil.getEndOfDay(endDate);

		Map<String, List<MetricStats>> trendData = new HashMap<String, List<MetricStats>>();
		List<ActivityMetricConfig> activityMetrics = getActivityMetricConfig();

		for (ActivityMetricConfig activityMetricConfig : activityMetrics) {
			MetricStatsCalculator metricStatCalc = metricCalcFactory.getMetricCalculator(activityMetricConfig.getMetricCode());
			if( metricStatCalc == null ){
				LOGGER.debug(" No Implementation Found for the Activity "+activityMetricConfig);
				continue;
			}
			List<MetricStats> metricStat = getTrendData(metricStatCalc, trendType, startDate, endDate);
			
			if(metricStat == null) {
				LOGGER.debug(" Implementation Returns null for the activity so continuing "+activityMetricConfig);
				continue;
			}
			trendData.put(metricStatCalc.getActivityCode().getCode(), metricStat);
		}
		List<Map<String, Long>> activityTrend = transformDataToUIFormat(trendData, filterActivityMetrices);
		return activityTrend;
	}

	private List<MetricStats> getTrendData(MetricStatsCalculator metricStatCalc, String trendType, Date startDate,
			Date endDate) {
		List<MetricStats> metricStat = null;
		
		if(ActivityTrend.DAILY.getValue().equalsIgnoreCase(trendType)) {
			metricStat = metricStatCalc.calculateDailyTrend(startDate, endDate);
		}
		else if(ActivityTrend.WEEKLY.getValue().equalsIgnoreCase(trendType)) {
			metricStat = metricStatCalc.calculateWeeklyTrend(startDate, endDate);
		}
		else if(ActivityTrend.MONTHLY.getValue().equalsIgnoreCase(trendType)) {
			metricStat = metricStatCalc.calculateMonthlyTrend(startDate, endDate);
		}
		return metricStat;
	}

	private List<Map<String, Long>> transformDataToUIFormat(Map<String, List<MetricStats>> trendData, List<String> filterActivityMetrices) {

		List<Map<String, Long>> activityTrend = new ArrayList<Map<String, Long>>(10);
		for(Entry<String, List<MetricStats>> trendMetric : trendData.entrySet()) {
			String metricCode = trendMetric.getKey();
			if(filterActivityMetrices.contains(metricCode)) {
				List<MetricStats> metricStats = trendMetric.getValue();
				int metricCounter=0;
				for (MetricStats metricStat : metricStats) {
					int size = activityTrend.size();
					Map<String, Long> trendMap;
					if(size <= metricCounter) {
						trendMap = new HashMap<String, Long>();
						activityTrend.add(trendMap);	
					}
					else{
						trendMap = activityTrend.get(metricCounter);
					}
					metricCounter++;
					trendMap.put(metricStat.getMetricCode(), metricStat.getMetricValue());		
					trendMap.put(AppConstants.ACTIVITY_TREND_KEY, new Long(metricCounter));
				}
			}
		}
		return activityTrend;
	}
	
}