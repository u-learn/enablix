package com.enablix.analytics.correlation.matcher;

import java.util.List;
import java.util.Map;

import com.enablix.core.commons.xsdtopojo.RelatedUserType;
import com.enablix.services.util.template.TemplateWrapper;

public interface UserMatcher {

	List<Map<String, Object>> findMatchingUsers(TemplateWrapper template, String userQualifiedId, RelatedUserType relatedUserDef,
			MatchInputRecord matchInput);
	
}
