package com.enablix.analytics.search.es;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Requests;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemClassType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.mongo.AppConstants;
import com.enablix.services.util.DatastoreUtil;
import com.enablix.services.util.TemplateUtil;


public class ESQueryBuilder {
	
	private static final String BOOST_OPERATOR = "^";

	private static final Logger LOGGER = LoggerFactory.getLogger(ESQueryBuilder.class);

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
		
		Map<String, Integer> fieldBoostIndex = new HashMap<>();
		getFieldNames(fieldBoostIndex, 
				template.getDataDefinition().getContainer());
		MultiMatchQueryBuilder multiMatchQuery = QueryBuilders.multiMatchQuery(
				searchText, convertToMultiMatchBoostedFields(fieldBoostIndex));
		
		//MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("_all", searchText);
		SearchSourceBuilder searchSource = SearchSourceBuilder.searchSource().query(multiMatchQuery);
		
		searchRequest.source(searchSource);
		
		return searchRequest;
	}
	
	private String[] convertToMultiMatchBoostedFields(Map<String, Integer> fieldBoostIndex) {
		
		String[] boostedFieldSearch = new String[fieldBoostIndex.size()];
		
		int indx = 0;
		for (Map.Entry<String, Integer> entry : fieldBoostIndex.entrySet()) {
			boostedFieldSearch[indx++] = entry.getKey() + BOOST_OPERATOR + entry.getValue();
		}
		
		return boostedFieldSearch;
	}
	
	private void getFieldNames(Map<String, Integer> fieldSearchBoost, List<ContainerType> containers) {
		
		for (ContainerType container : containers) {
			
			for (ContentItemType contentItem : container.getContentItem()) {
				
				if (contentItem.getType() == ContentItemClassType.DATE_TIME
						|| contentItem.getType() == ContentItemClassType.NUMERIC) {
					// do not search on Data and numeric fields
					continue;
				}
				
				Integer itemBoostValue = contentItem.getSearchBoost() == null ? 1 
											: contentItem.getSearchBoost().intValue();
				
				String esFieldName = getESFieldName(contentItem, container);
				
				if (fieldSearchBoost.containsKey(esFieldName)) {
				
					Integer boostValue = fieldSearchBoost.get(esFieldName);
					if (itemBoostValue > boostValue) {
						fieldSearchBoost.put(esFieldName, itemBoostValue);
					}
					
				} else {
					fieldSearchBoost.put(esFieldName, itemBoostValue);
				}
			}
		
			getFieldNames(fieldSearchBoost, container.getContainer());
		}
	}
	
	private String getESFieldName(ContentItemType contentItem, ContainerType container) {
		
		String fieldName = contentItem.getQualifiedId();
		
		ContainerType holdingContainer = container;
		if (!container.isReferenceable()) {
			holdingContainer = TemplateUtil.findReferenceableParentContainer(
					template.getDataDefinition(), container.getQualifiedId());
		}
		
		String containerQId = holdingContainer.getQualifiedId();

		// container name = c1.c2
		// field name = c1.c2.f1, hence remove the container prefix from field
		if (fieldName.startsWith(containerQId)) {
			fieldName = fieldName.substring(containerQId.length() + 1);
		}
		
		if (contentItem.getType() == ContentItemClassType.BOUNDED) {
			fieldName += ".label";
		} else if (contentItem.getType() == ContentItemClassType.DOC) {
			fieldName += ".name";
		}
		
		LOGGER.debug("ES Query field name: {}", fieldName);
		return fieldName;
	}

	private String getIndexName() {
		return DatastoreUtil.getTenantAwareDbName(AppConstants.BASE_DB_NAME);
	}
	
	private String[] getTypes() {
		List<String> allCollections = TemplateUtil.findContentCollectionNames(template);
		return allCollections.toArray(new String[0]);
	}
	
}
