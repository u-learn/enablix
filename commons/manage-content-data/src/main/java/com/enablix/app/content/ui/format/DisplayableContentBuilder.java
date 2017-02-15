package com.enablix.app.content.ui.format;

import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.ui.DisplayableContent;
import com.enablix.services.util.template.TemplateWrapper;

public interface DisplayableContentBuilder {

	DisplayableContent build(TemplateWrapper template, ContentDataRecord record, DisplayContext ctx);
	
}
