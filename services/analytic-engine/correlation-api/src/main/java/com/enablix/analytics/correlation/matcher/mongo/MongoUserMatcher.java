package com.enablix.analytics.correlation.matcher.mongo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.enablix.analytics.correlation.matcher.MatchInputRecord;
import com.enablix.analytics.correlation.matcher.UserMatcher;
import com.enablix.core.commons.xsdtopojo.FilterCriteriaType;
import com.enablix.core.commons.xsdtopojo.RelatedUserType;
import com.enablix.core.domain.security.authorization.UserProfile;
import com.enablix.core.mongo.dao.GenericDao;
import com.enablix.services.util.template.TemplateWrapper;

@Component
public class MongoUserMatcher implements UserMatcher {

	@Autowired
	private GenericDao genericDao;
	
	@Autowired
	private MongoCorrelationQueryBuilder mongoCorrQueryBuilder;
	
	private UserFilterAttributeIdResolver attributeIdResolver = new UserFilterAttributeIdResolver();
	
	@Override
	public List<UserProfile> findMatchingUsers(TemplateWrapper template, String userQualifiedId, 
			RelatedUserType relatedUserDef, MatchInputRecord matchInput) {
		
		FilterCriteriaType userFilterCriteria = relatedUserDef.getFilterCriteria();
		
		// Now, search for related user
	
		Criteria searchRequest = mongoCorrQueryBuilder.build(template, 
				userQualifiedId, attributeIdResolver, userFilterCriteria, null, matchInput);
		
		List<UserProfile> matchedUsers = genericDao.findByCriteria(searchRequest, UserProfile.class);
		
		return matchedUsers;
	}

}
