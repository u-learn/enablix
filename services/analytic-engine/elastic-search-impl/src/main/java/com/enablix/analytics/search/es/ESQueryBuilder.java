package com.enablix.analytics.search.es;

import java.util.List;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Requests;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.mongo.AppConstants;
import com.enablix.services.util.DatastoreUtil;
import com.enablix.services.util.TemplateUtil;


public class ESQueryBuilder {

	private String searchText;
	private ContentTemplate template;
	
	private ESQueryBuilder(String searchText, ContentTemplate template) {
		this.searchText = searchText;
		this.template = template;
	}
	
	public static ESQueryBuilder builder(String searchText, ContentTemplate template) {
		ESQueryBuilder builder = new ESQueryBuilder(searchText, template);
		return builder;
	}
	
	public SearchRequest build() {
		
		SearchRequest searchRequest = Requests.searchRequest(getIndexName())
					.searchType(SearchType.DFS_QUERY_THEN_FETCH)
					.types(getTypes());
		
		MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("_all", searchText);
		SearchSourceBuilder searchSource = SearchSourceBuilder.searchSource().query(matchQuery);
		
		searchRequest.source(searchSource);
		
		return searchRequest;
	}
	
	private String getIndexName() {
		return DatastoreUtil.getTenantAwareDbName(AppConstants.BASE_DB_NAME);
	}
	
	private String[] getTypes() {
		List<String> allCollections = TemplateUtil.findAllCollectionNames(template);
		return allCollections.toArray(new String[0]);
	}
	
}
