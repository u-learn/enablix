package com.enablix.commons.util.date;

import java.util.Date;

public class Period {

	private Date startDate;
	private Date endDate;
	
	public Period(Date startDate, Date endDate) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	
}
