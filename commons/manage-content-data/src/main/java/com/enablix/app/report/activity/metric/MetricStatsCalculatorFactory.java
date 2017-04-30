package com.enablix.app.report.activity.metric;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.beans.SpringBackedAbstractFactory;

@Component
public class MetricStatsCalculatorFactory extends SpringBackedAbstractFactory<MetricStatsCalculator> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MetricStatsCalculatorFactory.class);
	
	public MetricStatsCalculator getMetricCalculator(String activityCode) {
		
		for (MetricStatsCalculator metricCalculator : registeredInstances()) {
			String metricCode =  metricCalculator.getActivityCode().getCode();
			if (metricCode.equalsIgnoreCase(activityCode)) {
				return metricCalculator;
			}
		}

		LOGGER.error(" No Metric Calculator found for activity code {}", activityCode);
		return null;
	}
	
	@Override
	protected Class<MetricStatsCalculator> lookupForType() {
		return MetricStatsCalculator.class;
	}

}
