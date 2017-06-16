package com.enablix.core.domain.report.activitymetric;

import com.enablix.commons.util.date.DateDimension;

public class TrendAggMetric {
	
	private int dayOfYear;
	private int weekOfYear;
	private int monthOfYear;
	private int year;
	private long value;
	
	public TrendAggMetric() {

	}
	
	public TrendAggMetric(DateDimension dateDimension, long value) {
		this.dayOfYear = dateDimension.getDayOfYear();
		this.monthOfYear = dateDimension.getMonthOfYear();
		this.weekOfYear = dateDimension.getWeekOfYear();
		this.year = dateDimension.getYear();
		this.value = value;
	}


	public int getDayOfYear() {
		return dayOfYear;
	}

	public void setDayOfYear(int dayOfYear) {
		this.dayOfYear = dayOfYear;
	}

	public int getWeekOfYear() {
		return weekOfYear;
	}

	public void setWeekOfYear(int weekOfYear) {
		this.weekOfYear = weekOfYear;
	}

	public int getMonthOfYear() {
		return monthOfYear;
	}

	public void setMonthOfYear(int monthOfYear) {
		this.monthOfYear = monthOfYear;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}
	
}