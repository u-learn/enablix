package com.enablix.app.content.ui.format;

import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.ui.DisplayableContent;

public interface DisplayableContentBuilder {

	DisplayableContent build(TemplateFacade template, ContentDataRecord record, DisplayContext ctx);
	
}
