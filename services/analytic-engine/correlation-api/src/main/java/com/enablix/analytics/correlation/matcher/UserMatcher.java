package com.enablix.analytics.correlation.matcher;

import java.util.List;

import com.enablix.core.commons.xsdtopojo.RelatedUserType;
import com.enablix.core.domain.security.authorization.UserProfile;
import com.enablix.services.util.template.TemplateWrapper;

public interface UserMatcher {

	List<UserProfile> findMatchingUsers(TemplateWrapper template, String userQualifiedId, RelatedUserType relatedUserDef,
			MatchInputRecord matchInput);
	
}
