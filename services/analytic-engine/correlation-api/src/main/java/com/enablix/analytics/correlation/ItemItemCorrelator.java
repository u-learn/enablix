package com.enablix.analytics.correlation;

import com.enablix.core.api.ContentDataRef;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;

public interface ItemItemCorrelator {

	void correlateItem(ContentTemplate template, ContentDataRef item);
	
}
