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

	List<ReportStats> reportStats ;
	
	public ActivityMetric() {
		reportStats = new ArrayList<ReportStats>();
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
	
	public List<ReportStats> getReportStats() {
		return reportStats;
	}

	public void setReportStats(List<ReportStats> reportStats) {
		this.reportStats = reportStats;
	}
}
