package com.enablix.commons.util.date;

public class DateDimension {
	
	private int dayOfWeek;
	private int dayOfMonth;
	private int dayOfYear;
	private int weekOfMonth;
	private int weekOfYear;
	private int monthOfYear;
	private int year;
	
	public int getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	public int getDayOfMonth() {
		return dayOfMonth;
	}
	public void setDayOfMonth(int dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}
	public int getDayOfYear() {
		return dayOfYear;
	}
	public void setDayOfYear(int dayOfYear) {
		this.dayOfYear = dayOfYear;
	}
	public int getWeekOfMonth() {
		return weekOfMonth;
	}
	public void setWeekOfMonth(int weekOfMonth) {
		this.weekOfMonth = weekOfMonth;
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
}