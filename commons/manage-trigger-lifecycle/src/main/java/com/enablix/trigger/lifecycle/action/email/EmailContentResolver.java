package com.enablix.trigger.lifecycle.action.email;

import java.util.List;

import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.BaseEmailContentType;
import com.enablix.data.view.DataView;

public interface EmailContentResolver<T extends BaseEmailContentType> {

	List<ContentDataRecord> getEmailContent(T emailContentDef, 
			TemplateFacade template, ContentDataRef triggerItem, DataView view);
	
	boolean canHandle(BaseEmailContentType contentDef);
	
}
