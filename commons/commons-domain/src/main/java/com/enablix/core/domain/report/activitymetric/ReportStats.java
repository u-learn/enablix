package com.enablix.core.domain.report.activitymetric;

public class ReportStats {
	
	private String _id;
	
	private Integer reportValue;
	
	public ReportStats(String _id, Integer reportValue) {
		this._id = _id;
		this.reportValue = reportValue;
	}
	public String getReportName() {
		return _id;
	}
	public void setReportName(String reportName) {
		this._id = reportName;
	}
	public Integer getReportValue() {
		return reportValue;
	}
	public void setReportValue(Integer reportValue) {
		this.reportValue = reportValue;
	}
}
