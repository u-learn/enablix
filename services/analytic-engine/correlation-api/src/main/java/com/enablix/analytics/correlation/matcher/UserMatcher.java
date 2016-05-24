package com.enablix.analytics.correlation.matcher;

import java.util.List;
import java.util.Map;

import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.commons.xsdtopojo.RelatedUserType;

public interface UserMatcher {

	List<Map<String, Object>> findMatchingUsers(ContentTemplate template, String userQualifiedId, RelatedUserType relatedUserDef,
			MatchInputRecord matchInput);
	
}
