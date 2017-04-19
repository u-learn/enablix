package com.enablix.core.domain.report.activitymetric;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.commons.constants.AppConstants;
import com.enablix.core.domain.BaseDocumentEntity;

@Document(collection = AppConstants.ACTIVITY_METRIC)
public class ActivityMetric extends BaseDocumentEntity{
	
	private Date asOfDate;
	
	private boolean latest;

	private List<MetricStats> metricStats ;
	
	public ActivityMetric() {
		metricStats = new ArrayList<MetricStats>();
	}
	
	public Date getAsOfDate() {
		return asOfDate;
	}

	public void setAsOfDate(Date asOfDate) {
		this.asOfDate = asOfDate;
	}

	public boolean isLatest() {
		return latest;
	}

	public void setLatest(boolean latest) {
		this.latest = latest;
	}

	public List<MetricStats> getMetricStats() {
		return metricStats;
	}

	public void setMetricStats(List<MetricStats> metricStats) {
		this.metricStats = metricStats;
	}
}
