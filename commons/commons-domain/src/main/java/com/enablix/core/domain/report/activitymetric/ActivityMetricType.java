package com.enablix.core.domain.report.activitymetric;

import java.util.ArrayList;
import java.util.List;

import com.enablix.commons.constants.AppConstants.MetricType;

public class ActivityMetricType {

	private String metricCode;
	
	private String metricName;

	public ActivityMetricType(String metricCode, String metricName) {
		super();
		this.metricCode = metricCode;
		this.metricName = metricName;
	}

	public String getMetricCode() {
		return metricCode;
	}

	public void setMetricCode(String metricCode) {
		this.metricCode = metricCode;
	}

	public String getMetricName() {
		return metricName;
	}

	public void setMetricName(String metricName) {
		this.metricName = metricName;
	}
	
	@Override
	public String toString() {
		return "ActivityMetricType [metricCode=" + metricCode + ", metricName=" + metricName + "]";
	}

	private static List<ActivityMetricType> allTypes;
	
	static {
		allTypes = new ArrayList<ActivityMetricType>();
		for (MetricType type : MetricType.values()) {
			allTypes.add(new ActivityMetricType(type.getCode(), type.getValue()));
		}
	}
	
	public static List<ActivityMetricType> getAllTypes() {
		return allTypes;
	}
}
