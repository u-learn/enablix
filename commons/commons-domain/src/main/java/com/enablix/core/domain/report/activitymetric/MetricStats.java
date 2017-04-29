package com.enablix.core.domain.report.activitymetric;

public class MetricStats implements Comparable<MetricStats>{
	
	private String metricCode;
	
	private String metricName;
	
	private Long metricValue;
	
	public MetricStats(String metricCode, String metricName, Long metricValue) {
		this.metricCode = metricCode;
		this.metricName = metricName;
		this.metricValue = metricValue;
	}
	
	public Long getMetricValue() {
		return metricValue;
	}
	
	public void setMetricValue(Long metricValue) {
		this.metricValue = metricValue;
	}

	@Override
	public int compareTo(MetricStats metricStat) {
		return this.getMetricName().compareTo(metricStat.getMetricName());
	}

	public String getMetricName() {
		return metricName;
	}

	public void setMetricName(String metricName) {
		this.metricName = metricName;
	}
	
	public String getMetricCode() {
		return metricCode;
	}

	public void setMetricCode(String metricCode) {
		this.metricCode = metricCode;
	}
}
