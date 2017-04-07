package com.enablix.analytics.correlation.matcher;

import java.util.List;

import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.RelatedUserType;
import com.enablix.core.domain.security.authorization.UserProfile;

public interface UserMatcher {

	List<UserProfile> findMatchingUsers(TemplateFacade template, String userQualifiedId, RelatedUserType relatedUserDef,
			MatchInputRecord matchInput);
	
}
