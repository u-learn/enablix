package com.enablix.app.content.ui;

import com.enablix.app.content.label.ContentLabelResolver;
import com.enablix.core.api.ContentDataRef;

public interface NavigableContentBuilder {

	NavigableContent build(ContentDataRef data, ContentLabelResolver labelResolver);
	
}
