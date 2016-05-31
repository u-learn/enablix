package com.enablix.core.domain.trigger;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.commons.xsdtopojo.ContentTriggerDefType;
import com.enablix.core.domain.BaseDocumentEntity;

@Document(collection = "ebx_trigger_lifecycle_rule")
public class TriggerLifecycleRule extends BaseDocumentEntity {

	private ContentTriggerDefType contentTriggerRule;

	public ContentTriggerDefType getContentTriggerRule() {
		return contentTriggerRule;
	}

	public void setContentTriggerRule(ContentTriggerDefType triggerRule) {
		this.contentTriggerRule = triggerRule;
	}

}
