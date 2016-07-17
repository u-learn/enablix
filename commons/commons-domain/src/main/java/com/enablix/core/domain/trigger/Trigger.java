package com.enablix.core.domain.trigger;

import java.util.Date;

import com.enablix.core.commons.xsdtopojo.TriggerTypeEnum;

public abstract class Trigger {

	private Date triggerTime;
	
	public Trigger(Date triggerTime) {
		this.triggerTime = triggerTime;
	}
	
	public Date getTriggerTime() {
		return triggerTime;
	}

	public abstract TriggerTypeEnum getTriggerType();
	
}
