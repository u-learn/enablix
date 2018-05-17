package com.enablix.core.domain.content.quality;

import java.util.Date;

public class ContentObsoleteInfo extends AlertInfo {

	private Date obsoleteOn;

	public Date getObsoleteOn() {
		return obsoleteOn;
	}

	public void setObsoleteOn(Date obsoleteOn) {
		this.obsoleteOn = obsoleteOn;
	}
	
}
