package com.enablix.bayes;

import java.util.Calendar;
import java.util.Date;

public class InitializationContext {

	private Date runAsDate;
	
	private Calendar runAsDateCalendar;
	
	public Date getRunAsDate() {
		return runAsDate;
	}

	public void setRunAsDate(Date runAsDate) {
		
		this.runAsDate = runAsDate;
		
		this.runAsDateCalendar = Calendar.getInstance();
		this.runAsDateCalendar.setTime(runAsDate);
	}
	
	public Date getAdjustedRunAsDate(int daysAdjustment) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(runAsDate);
		cal.add(Calendar.DAY_OF_YEAR, daysAdjustment);
		return cal.getTime();
	}

	public Calendar getRunAsDateCalendar() {
		return runAsDateCalendar;
	}

	
}
