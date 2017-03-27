package com.enablix.analytics.correlation.impl;

import java.util.List;

import com.enablix.core.api.ContentDataRef;
import com.enablix.core.commons.xsdtopojo.ItemUserCorrelationRuleType;
import com.enablix.core.commons.xsdtopojo.TagsType;
import com.enablix.core.correlation.ItemUserCorrelation;
import com.enablix.core.domain.security.authorization.UserProfile;

public interface ItemUserCorrelationRecorder {

	void removeItemUserCorrelation(ContentDataRef item);
	
	void removeItemUserCorrelationByRule(ContentDataRef item, ItemUserCorrelationRuleType rule);

	ItemUserCorrelation recordItemUserCorrelation(ContentDataRef item, UserProfile user,
			ItemUserCorrelationRuleType rule, TagsType tags);

	ItemUserCorrelation recordItemUserCorrelation(ContentDataRef item, UserProfile userProfile, List<String> tags);
	
}
