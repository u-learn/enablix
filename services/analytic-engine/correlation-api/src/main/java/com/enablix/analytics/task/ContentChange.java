package com.enablix.analytics.task;

import com.enablix.core.api.ContentDataRef;

public class ContentChange extends Trigger {

	public static enum TriggerType {
		ADD, UPDATE
	}
	
	private ContentDataRef triggerItem;
	
	private TriggerType type;

	public ContentDataRef getTriggerItem() {
		return triggerItem;
	}

	public void setTriggerItem(ContentDataRef triggerItem) {
		this.triggerItem = triggerItem;
	}

	public TriggerType getType() {
		return type;
	}

	public void setType(TriggerType type) {
		this.type = type;
	}
	
}
