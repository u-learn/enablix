package com.enablix.core.api;

import java.util.Date;

public class ContentTypeCoverageDTO {

	private String id;
	
	private String containerLabel;
	
	private Long count;
	
	private Date lastCreateDt;
	
	private Date lastUpdateDt;
	
	private Date asOfDate;
	
	public ContentTypeCoverageDTO() {
		super();
		this.count = 0L;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContainerLabel() {
		return containerLabel;
	}

	public void setContainerLabel(String containerLabel) {
		this.containerLabel = containerLabel;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public Date getLastCreateDt() {
		return lastCreateDt;
	}

	public void setLastCreateDt(Date lastCreateDt) {
		this.lastCreateDt = lastCreateDt;
	}

	public Date getLastUpdateDt() {
		return lastUpdateDt;
	}

	public void setLastUpdateDt(Date lastUpdateDt) {
		this.lastUpdateDt = lastUpdateDt;
	}

	public Date getAsOfDate() {
		return asOfDate;
	}

	public void setAsOfDate(Date asOfDate) {
		this.asOfDate = asOfDate;
	}
	
}
