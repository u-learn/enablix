package com.enablix.trigger.lifecycle.action.email;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.ContentDataManager;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.commons.xsdtopojo.BaseEmailContentType;
import com.enablix.core.commons.xsdtopojo.EmailContentTriggerEntityType;
import com.enablix.services.util.template.TemplateWrapper;

@Component
public class TriggerEntityEmailContentResolver implements EmailContentResolver<EmailContentTriggerEntityType> {

	@Autowired
	private ContentDataManager contentDataMgr;
	
	@Override
	public List<ContentDataRecord> getEmailContent(EmailContentTriggerEntityType emailContentDef,
			TemplateWrapper template, ContentDataRef triggerItem) {
		
		Map<String, Object> triggerRecord = contentDataMgr.getContentRecord(triggerItem, template);
		
		List<ContentDataRecord> emailContent = new ArrayList<>();
		if (triggerRecord != null && !triggerRecord.isEmpty()) {
			ContentDataRecord record = new ContentDataRecord(template.getId(), 
					triggerItem.getContainerQId(), triggerRecord);
			emailContent.add(record);
		}
		
		return emailContent;
	}

	@Override
	public boolean canHandle(BaseEmailContentType contentDef) {
		return contentDef instanceof EmailContentTriggerEntityType;
	}

}
