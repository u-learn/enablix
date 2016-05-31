package com.enablix.analytics.correlation.impl;

import java.util.List;

import com.enablix.core.api.ContentDataRef;
import com.enablix.core.commons.xsdtopojo.ItemUserCorrelationRuleType;
import com.enablix.core.commons.xsdtopojo.TagsType;
import com.enablix.core.correlation.ItemUserCorrelation;

public interface ItemUserCorrelationRecorder {

	ItemUserCorrelation recordItemUserCorrelation(ContentDataRef item, 
			ContentDataRef userRef, ItemUserCorrelationRuleType rule, TagsType tags);

	ItemUserCorrelation recordItemUserCorrelation(ContentDataRef item, ContentDataRef userRef, List<String> tags);

}
