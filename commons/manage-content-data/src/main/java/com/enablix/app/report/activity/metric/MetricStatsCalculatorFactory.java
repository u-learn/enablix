package com.enablix.app.report.activity.metric;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.beans.SpringBackedBeanRegistry;

@Component
public class MetricStatsCalculatorFactory extends SpringBackedBeanRegistry<MetricStatsCalculator> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MetricStatsCalculatorFactory.class);
	
	private Map<String, MetricStatsCalculator> metricStore = new HashMap<>();
	
	public MetricStatsCalculator getMetricCalculator(String activityCode) {
		
		return metricStore.get(activityCode);
		
	}
	
	@Override
	protected Class<MetricStatsCalculator> lookupForType() {
		return MetricStatsCalculator.class;
	}

	@Override
	protected void registerBean(MetricStatsCalculator bean) {
		MetricStatsCalculator metric = metricStore.get(bean.metricType().getCode());
		
		if (metric != null) {
			LOGGER.error("More than one metric with code [{}]", bean.metricType());
			throw new IllegalStateException("Multiple metric with same code [" + bean.metricType() + "]");
		}
		
		metricStore.put(bean.metricType().getCode(), bean);
	}

}
