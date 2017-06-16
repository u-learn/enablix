package com.enablix.app.report.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.enablix.commons.util.date.DateDimension;
import com.enablix.commons.util.date.DateUtil;
import com.enablix.core.domain.report.activitymetric.MetricStats;
import com.enablix.core.domain.report.activitymetric.TrendAggMetric;

public class TrendUtil {
	
	public static List<MetricStats> updateDailyMissingValues(List<TrendAggMetric> trendAggMetricLst, 
			Date startDate, Date endDate, String metricCode, String metricName) {
		
		List<MetricStats> metricStatLst = new ArrayList<MetricStats>();
        
		int diffCounter = 0;
        
        while (startDate.compareTo(endDate)<= 0 ) {
            
        	TrendAggMetric trendAggMetric;
        	MetricStats metricStat;
        	
        	DateDimension startDateDimension = DateUtil.getDateDimension(startDate);
        	
        	if (trendAggMetricLst.size() <= diffCounter) {
        		trendAggMetric = new TrendAggMetric(startDateDimension, 0);
        	} else {
        		trendAggMetric = trendAggMetricLst.get(diffCounter);
        	}
        	
        	if (startDateDimension.getDayOfYear() == trendAggMetric.getDayOfYear()) {
            	
        		metricStat = new MetricStats(metricCode, metricName, trendAggMetric.getValue(), startDate);
            	metricStatLst.add(metricStat);
            	
            	startDate = DateUtil.getNextDate(startDate);
            	
        	} else {
        		
        		metricStat = new MetricStats(metricCode, metricName, new Long(0), startDate);
        		metricStatLst.add(metricStat);
        		
        		startDate = DateUtil.getNextDate(startDate);
        		continue;
        	}
        	
        	diffCounter++;
        }
        
		return metricStatLst;
	}
	
	public static List<MetricStats> updateWeeklyMissingValues(
			List<TrendAggMetric> trendAggMetricLst, Date startDate, 
			Date endDate, String metricCode, String metricName) {
		
		List<MetricStats> metricStatLst = new ArrayList<MetricStats>();
        
		int diffCounter = 0;
        
		startDate = DateUtil.shiftToFirstDayOfWeek(startDate);
        
		while (startDate.compareTo(endDate) <= 0) {
            
			TrendAggMetric trendAggMetric;
            MetricStats metricStat;

            DateDimension startDateDimension = DateUtil.getDateDimension(startDate);
        	
            if (trendAggMetricLst.size() <= diffCounter) {
        		trendAggMetric = new TrendAggMetric(startDateDimension, 0);
        	} else {
        		trendAggMetric = trendAggMetricLst.get(diffCounter);
        	}
        	
        	if (startDateDimension.getWeekOfYear() == trendAggMetric.getWeekOfYear()) {
            	
        		metricStat = new MetricStats(metricCode, metricName, trendAggMetric.getValue(), startDate);
            	metricStatLst.add(metricStat);
            	
            	startDate = DateUtil.getNextWeekDate(startDate);
            	
        	} else {
        		
        		metricStat = new MetricStats(metricCode, metricName, new Long(0), startDate);
        		metricStatLst.add(metricStat);
        		
        		startDate = DateUtil.getNextWeekDate(startDate);
        		continue;
        	}
        	
        	diffCounter++;
        }
		
		return metricStatLst;
	}

	public static List<MetricStats> updateMonthlyMissingValues(
			List<TrendAggMetric> trendAggMetricLst, Date startDate,
			Date endDate, String metricCode, String metricName) {
		
		List<MetricStats> metricStatLst = new ArrayList<MetricStats>();
        
		int diffCounter = 0;
        
		startDate = DateUtil.shiftToFirstDayOfMonth(startDate);
        
		while (startDate.compareTo(endDate) <= 0 ) {
            
			TrendAggMetric trendAggMetric;
        	MetricStats metricStat;
        	
        	DateDimension startDateDimension = DateUtil.getDateDimension(startDate);
        	
        	if (trendAggMetricLst.size() <= diffCounter) {
        		trendAggMetric = new TrendAggMetric(startDateDimension, 0);
        	} else {
        		trendAggMetric = trendAggMetricLst.get(diffCounter);
        	}
        	
        	if (startDateDimension.getMonthOfYear() == trendAggMetric.getMonthOfYear()) {
            	
        		metricStat = new MetricStats(metricCode, metricName, trendAggMetric.getValue(), startDate);
            	metricStatLst.add(metricStat);
            	
            	startDate = DateUtil.getNextMonthDate(startDate);
            	
        	} else {
        		
        		metricStat = new MetricStats(metricCode, metricName, new Long(0), startDate);
        		metricStatLst.add(metricStat);
        		
        		startDate = DateUtil.getNextMonthDate(startDate);
        		continue;
        	}
        	
        	diffCounter++;
        }
		
		return metricStatLst;
	}
	
}
