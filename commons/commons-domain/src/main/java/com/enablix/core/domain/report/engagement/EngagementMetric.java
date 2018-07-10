package com.enablix.core.domain.report.engagement;

import java.util.List;

public class EngagementMetric {

	private String label;
	
	private long count;
	
	private List<MetricItem> distribution;
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public List<MetricItem> getDistribution() {
		return distribution;
	}

	public void setDistribution(List<MetricItem> distribution) {
		this.distribution = distribution;
	}
	
}
