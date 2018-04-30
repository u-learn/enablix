package com.enablix.app.mail.generic;

import java.util.Map;

import com.enablix.core.mail.BasicEmailVelocityInput;

public class ActivityMetricMailInput extends BasicEmailVelocityInput {

	private Map<String, Object> metricValues;
	
	public Map<String, Object> getMetricValues() {
		return metricValues;
	}

	public void setMetricValues(Map<String, Object> metricValues) {
		this.metricValues = metricValues;
	}

}
