package com.enablix.core.domain.report.activitymetric;

public class MetricStats {
	
	private String _id;
	
	private Integer metricValue;
	
	public MetricStats(String _id, Integer metricValue) {
		this._id = _id;
		this.metricValue = metricValue;
	}
	
	public String getMetricName() {
		return _id;
	}
	
	public void setMetricName(String metricName) {
		this._id = metricName;
	}
	
	public Integer getMetricValue() {
		return metricValue;
	}
	
	public void setMetricValue(Integer metricValue) {
		this.metricValue = metricValue;
	}
}
