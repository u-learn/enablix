package com.enablix.app.content.ui;

import java.util.Map;

import com.enablix.app.content.label.ContentLabelResolver;
import com.enablix.core.api.ContentDataRef;

public interface NavigableContentBuilder {

	NavigableContent build(ContentDataRef data, ContentLabelResolver labelResolver);
	
	NavigableContent build(Map<String, Object> record, String templateId, String qId, ContentLabelResolver labelResolver);
	
}
