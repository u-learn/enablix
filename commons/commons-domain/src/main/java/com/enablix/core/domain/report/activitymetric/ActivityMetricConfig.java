package com.enablix.core.domain.report.activitymetric;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.commons.constants.AppConstants;
import com.enablix.core.domain.BaseDocumentEntity;

@Document(collection = AppConstants.ACTIVITY_METRIC_CONFIG)
public class ActivityMetricConfig extends BaseDocumentEntity {

	private String metricCode;

	private String metricName;

	private String metricQueryFn;

	private Date startDate;

	private Date runDate;
	
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

	public String getMetricQueryFn() {
		return metricQueryFn;
	}

	public void setMetricQueryFn(String metricQueryFn) {
		this.metricQueryFn = metricQueryFn;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getRunDate() {
		return runDate;
	}

	public void setRunDate(Date runDate) {
		this.runDate = runDate;
	}
}
