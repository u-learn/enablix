package com.enablix.core.domain.trigger;

import java.util.Date;

import com.enablix.core.api.ContentDataRef;
import com.enablix.core.commons.xsdtopojo.TriggerTypeEnum;

public class ContentChange extends Trigger {

	public static enum TriggerType {
		ADD, UPDATE
	}
	
	private ContentDataRef triggerItem;
	
	private TriggerType type;
	
	public ContentChange(Date triggerTime) {
		super(triggerTime);
	}
	
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

	@Override
	public TriggerTypeEnum getTriggerType() {
		return type == TriggerType.ADD ? TriggerTypeEnum.CONTENT_ADD
					: TriggerTypeEnum.CONTENT_UPDATE;
	}
	
}
