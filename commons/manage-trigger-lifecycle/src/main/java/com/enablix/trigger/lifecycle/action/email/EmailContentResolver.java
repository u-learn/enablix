package com.enablix.trigger.lifecycle.action.email;

import java.util.List;

import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.commons.xsdtopojo.BaseEmailContentType;
import com.enablix.services.util.template.TemplateWrapper;

public interface EmailContentResolver<T extends BaseEmailContentType> {

	List<ContentDataRecord> getEmailContent(T emailContentDef, TemplateWrapper template, ContentDataRef triggerItem);;
	
	boolean canHandle(BaseEmailContentType contentDef);
	
}
