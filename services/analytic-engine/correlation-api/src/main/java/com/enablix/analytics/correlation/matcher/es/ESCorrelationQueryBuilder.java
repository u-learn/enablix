package com.enablix.analytics.correlation.matcher.es;

import java.util.List;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Requests;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.analytics.correlation.matcher.MatchInputRecord;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.FilterCriteriaType;
import com.enablix.core.commons.xsdtopojo.FilterOperatorType;
import com.enablix.core.commons.xsdtopojo.FilterType;
import com.enablix.core.commons.xsdtopojo.MatchCriteriaType;
import com.enablix.core.commons.xsdtopojo.MatchType;
import com.enablix.services.util.ElasticSearchUtil;

@Component
public class ESCorrelationQueryBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(ESCorrelationQueryBuilder.class);
	
	@Autowired
	private FilterToESQueryTx filterESQueryTx;
	
	public SearchRequest build(TemplateFacade template, String targetItemQId, 
			FilterCriteriaType filterCriteria, MatchCriteriaType matchCriteria, MatchInputRecord matchInput) {
		return build(template, ElasticSearchUtil.getIndexName(), targetItemQId, filterCriteria, matchCriteria, matchInput);
	}
	
	public SearchRequest build(TemplateFacade template, String indexName, String targetItemQId,
			FilterCriteriaType filterCriteria, MatchCriteriaType matchCriteria, MatchInputRecord matchInput) {
		
		String searchType = template.getCollectionName(targetItemQId);
		
		SearchRequest searchRequest = Requests.searchRequest(indexName)
				.searchType(SearchType.DFS_QUERY_THEN_FETCH).types(searchType);

		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		
		// Add filter criteria
		if (filterCriteria != null && CollectionUtil.isNotEmpty(filterCriteria.getFilter())) {
			
			for (FilterType filter : filterCriteria.getFilter()) {
				addFilterToQuer(template, targetItemQId, matchInput, qb, filter);
			}
			
		}

		// Add match criteria
		if (matchCriteria != null && CollectionUtil.isNotEmpty(matchCriteria.getMatch())) {
			// TODO: do we need match criteria or the filter criteria is sufficient
			for (MatchType match : matchCriteria.getMatch()) {
				addFilterToQuer(template, targetItemQId, matchInput, qb, match);
			}
		}
		
		SearchSourceBuilder searchSource = SearchSourceBuilder.searchSource().query(qb);
		searchRequest.source(searchSource);

		LOGGER.debug("Elastic search - index [{}], type [{}]", indexName, searchType);
		LOGGER.debug("Correlation query: {}", searchSource);
		
		return searchRequest;
	}

	private void addFilterToQuer(TemplateFacade template, String targetItemQId, 
			MatchInputRecord matchInput, BoolQueryBuilder qb, FilterType filter) {
		
		List<QueryBuilder> filterQbs = filterESQueryTx.createESQuery(
							filter, targetItemQId, matchInput, template);
		
		QueryBuilder filterParentQb = QueryBuilders.boolQuery();
		
		if (CollectionUtil.isNotEmpty(filterQbs)) {
		
			if (filterQbs.size() == 1) {
				
				// optimize for size 1
				filterParentQb = filterQbs.get(0);
				
			} else {
				
				// in case of multiple query builders for a filter, one match is sufficient
				// i.e. must have one match so we create a collection of should matches and 
				// add them as a must match
				BoolQueryBuilder shouldQbs = QueryBuilders.boolQuery();
				for (QueryBuilder filterQb : filterQbs) {
					shouldQbs.should(filterQb);
				}
				shouldQbs.minimumNumberShouldMatch(1);
				
				filterParentQb = shouldQbs;
			}
			
			// add it
			if (filter.getOperator() == FilterOperatorType.NOT_MATCH) {
				qb.mustNot(filterParentQb);
			} else {
				qb.filter(filterParentQb);
			}
		}
	}
	
}
