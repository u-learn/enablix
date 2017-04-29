package com.enablix.core.domain.report.activitymetric;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.commons.constants.AppConstants;
import com.enablix.core.domain.BaseDocumentEntity;

@Document(collection = AppConstants.ACTIVITY_METRIC_CONFIG)
public class ActivityMetricConfig extends BaseDocumentEntity {

	private String metricCode;

	private String metricName;

	private Date startDate;

	private Date nextRunDate;
	
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

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getNextRunDate() {
		return nextRunDate;
	}

	public void setNextRunDate(Date nextRunDate) {
		this.nextRunDate = nextRunDate;
	}

	@Override
	public String toString() {
		return "ActivityMetricConfig [metricCode=" + metricCode + ", metricName=" + metricName + ", "
				+ "startDate=" + startDate + ", nextRunDate=" + nextRunDate + "]";
	}
	
}
