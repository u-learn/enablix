package com.enablix.analytics.correlation;

import com.enablix.core.api.ContentDataRef;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;

public interface ItemUserCorrelator {

	void correlateUsers(ContentDataRef item, ContentTemplate template);
	
	void deleteCorrelationForItem(ContentDataRef item);
	
}
