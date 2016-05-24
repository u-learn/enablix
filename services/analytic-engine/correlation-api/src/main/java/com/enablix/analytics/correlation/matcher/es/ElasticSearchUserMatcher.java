package com.enablix.analytics.correlation.matcher.es;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.analytics.correlation.matcher.MatchInputRecord;
import com.enablix.analytics.correlation.matcher.UserMatcher;
import com.enablix.commons.util.json.JsonUtil;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.commons.xsdtopojo.FilterCriteriaType;
import com.enablix.core.commons.xsdtopojo.RelatedUserType;
import com.enablix.core.domain.user.User;

@Component
public class ElasticSearchUserMatcher implements UserMatcher {

	@Autowired
	private Client esClient;
	
	@Autowired
	private ESCorrelationQueryBuilder esCorrQueryBuilder;
	
	@Override
	public List<Map<String, Object>> findMatchingUsers(ContentTemplate template, String userQualifiedId, 
			RelatedUserType relatedUserDef, MatchInputRecord matchInput) {
		
		FilterCriteriaType userFilterCriteria = relatedUserDef.getFilterCriteria();
		
		// Now, search for related user
	
		SearchRequest searchRequest = esCorrQueryBuilder.build(template, 
				userQualifiedId, userFilterCriteria, null, matchInput);
		
		ActionFuture<SearchResponse> searchResult = esClient.search(searchRequest);
		SearchResponse result = searchResult.actionGet();
		
		List<Map<String, Object>> matchedUsers = new ArrayList<>();

		for (SearchHit hit : result.getHits()) {
			Map<String, Object> matchedUser = hit.getSource();
			matchedUsers.add(matchedUser);
		}
		
		return matchedUsers;
	}

}
