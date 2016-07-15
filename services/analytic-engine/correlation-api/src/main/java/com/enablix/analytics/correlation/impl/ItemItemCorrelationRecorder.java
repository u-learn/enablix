package com.enablix.analytics.correlation.impl;

import com.enablix.core.api.ContentDataRef;
import com.enablix.core.commons.xsdtopojo.ItemCorrelationRuleType;
import com.enablix.core.commons.xsdtopojo.TagsType;
import com.enablix.core.correlation.ItemItemCorrelation;

public interface ItemItemCorrelationRecorder {

	ItemItemCorrelation recordItemCorrelation(ContentDataRef item, ContentDataRef relatedItem, 
			ItemCorrelationRuleType itemCorrRule, TagsType tagsType);
	
	void removeItemCorrelations(ContentDataRef item);
	
}
