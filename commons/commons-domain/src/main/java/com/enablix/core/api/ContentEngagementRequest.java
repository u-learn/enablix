package com.enablix.core.api;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.enablix.core.api.SearchRequest.Pagination;

public class ContentEngagementRequest {

	private List<String> contentQIdIn;
	
	private String timePeriod;
	
	private Date startDate;
	
	private Pagination pagination;

	public List<String> getContentQIdIn() {
		return contentQIdIn;
	}

	public void setContentQIdIn(List<String> contentQIdIn) {
		this.contentQIdIn = contentQIdIn;
	}

	public String getTimePeriod() {
		return timePeriod;
	}

	public void setTimePeriod(String timePeriod) throws ParseException {
		this.timePeriod = timePeriod;
	}

	public Date getStartDate() {
		if (this.startDate == null) {
			DateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
			try {
				startDate = formatter.parse(timePeriod);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}
	
}
