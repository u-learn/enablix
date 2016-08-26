package com.enablix.app.content.ui.format;

import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.ui.DisplayableContent;

public interface DisplayableContentBuilder {

	DisplayableContent build(ContentTemplate template, ContentDataRecord record, DisplayContext ctx);
	
}
