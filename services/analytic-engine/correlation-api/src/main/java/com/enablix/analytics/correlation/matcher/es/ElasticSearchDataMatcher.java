package com.enablix.analytics.correlation.matcher.es;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.analytics.correlation.matcher.DataMatcher;
import com.enablix.analytics.correlation.matcher.MatchInputRecord;
import com.enablix.core.commons.xsdtopojo.FilterCriteriaType;
import com.enablix.core.commons.xsdtopojo.MatchCriteriaType;
import com.enablix.core.commons.xsdtopojo.PathItemType;
import com.enablix.core.commons.xsdtopojo.RelatedItemType;
import com.enablix.services.util.template.TemplateWrapper;

@Component
public class ElasticSearchDataMatcher implements DataMatcher {

	private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchDataMatcher.class);
	
	@Autowired
	private Client esClient;
	
	@Autowired
	private ESCorrelationQueryBuilder esCorrQueryBuilder;
	
	@Override
	public List<Map<String, Object>> findMatchingRecords(String matchItemQId, TemplateWrapper template,
			FilterCriteriaType filterCriteria, MatchInputRecord matchInput) {
		
		return searchMatchingRecords(matchItemQId, template, filterCriteria, null, matchInput);
	}

	private List<Map<String, Object>> searchMatchingRecords(String matchItemQId, TemplateWrapper template,
			FilterCriteriaType filterCriteria, MatchCriteriaType matchCriteria, MatchInputRecord matchInput) {
		
		LOGGER.debug("searching matched records of type: {}", matchItemQId);
		
		SearchRequest searchRequest = esCorrQueryBuilder.build(
				template, matchItemQId, filterCriteria, matchCriteria, matchInput);
		
		// TODO: pagination as Elastic search by default only return first 10 records
		ActionFuture<SearchResponse> searchResult = esClient.search(searchRequest);
		SearchResponse result = searchResult.actionGet();
		
		List<Map<String, Object>> matchedRecords = new ArrayList<>();

		for (SearchHit hit : result.getHits()) {
			Map<String, Object> matchedRec = hit.getSource();
			matchedRecords.add(matchedRec);
		}
		
		LOGGER.debug("Total matched records: {}", matchedRecords.size());
		
		return matchedRecords;
	}
	
	@Override
	public List<Map<String, Object>> findMatchingRecords(TemplateWrapper template, RelatedItemType relatedItemDef,
			MatchInputRecord matchInput) {
		
		return searchMatchingRecords(relatedItemDef.getQualifiedId(), template, 
				relatedItemDef.getFilterCriteria(), relatedItemDef.getMatchCriteria(), matchInput);
	}

	@Override
	public List<Map<String, Object>> findMatchingRecords(TemplateWrapper template, PathItemType pathItem,
			MatchInputRecord matchInput) {
		
		return searchMatchingRecords(pathItem.getQualifiedId(), template, 
				pathItem.getFilterCriteria(), null, matchInput);
	}

}
