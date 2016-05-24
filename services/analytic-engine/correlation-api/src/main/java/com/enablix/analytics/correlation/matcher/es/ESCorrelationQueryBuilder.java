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
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.commons.xsdtopojo.FilterCriteriaType;
import com.enablix.core.commons.xsdtopojo.FilterType;
import com.enablix.core.commons.xsdtopojo.MatchCriteriaType;
import com.enablix.core.commons.xsdtopojo.MatchType;
import com.enablix.services.util.ElasticSearchUtil;
import com.enablix.services.util.TemplateUtil;

@Component
public class ESCorrelationQueryBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(ESCorrelationQueryBuilder.class);
	
	@Autowired
	private FilterToESQueryTx filterESQueryTx;
	
	public SearchRequest build(ContentTemplate template, String targetItemQId, 
			FilterCriteriaType filterCriteria, MatchCriteriaType matchCriteria, MatchInputRecord matchInput) {
		return build(template, ElasticSearchUtil.getIndexName(), targetItemQId, filterCriteria, matchCriteria, matchInput);
	}
	
	public SearchRequest build(ContentTemplate template, String indexName, String targetItemQId,
			FilterCriteriaType filterCriteria, MatchCriteriaType matchCriteria, MatchInputRecord matchInput) {
		
		String searchType = TemplateUtil.resolveCollectionName(template, targetItemQId);
		
		SearchRequest searchRequest = Requests.searchRequest(indexName)
				.searchType(SearchType.DFS_QUERY_THEN_FETCH).types(searchType);

		BoolQueryBuilder qb = null;
		
		// Add filter criteria
		if (filterCriteria != null && CollectionUtil.isNotEmpty(filterCriteria.getFilter())) {
			
			qb = QueryBuilders.boolQuery();
			
			for (FilterType filter : filterCriteria.getFilter()) {
				
				List<QueryBuilder> filterQbs = filterESQueryTx.createESQuery(
									filter, targetItemQId, matchInput, template);
				
				for (QueryBuilder filterQb : filterQbs) {
					qb.filter(filterQb);
				}
			}
			
		}

		// Add match criteria
		if (matchCriteria != null && CollectionUtil.isNotEmpty(matchCriteria.getMatch())) {
			
			if (qb == null) {
				qb = QueryBuilders.boolQuery();
			}
			
			for (MatchType match : matchCriteria.getMatch()) {
				
				List<QueryBuilder> matchQbs = filterESQueryTx.createESQuery(
									match, targetItemQId, matchInput, template);
				
				for (QueryBuilder matchQb : matchQbs) {
					qb.must(matchQb);
				}
			}
			
		}
		
		SearchSourceBuilder searchSource = SearchSourceBuilder.searchSource().query(qb);
		searchRequest.source(searchSource);

		LOGGER.debug("Elastic search - index [{}], type [{}]", indexName, searchType);
		LOGGER.debug("Correlation query: {}", searchSource);
		
		return searchRequest;
	}
	
}
