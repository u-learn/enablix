package com.enablix.analytics.search.es;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemClassType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.es.view.ESDataView;
import com.enablix.es.view.ESDataViewOperation;
import com.enablix.services.util.ElasticSearchUtil;
import com.enablix.services.util.TemplateUtil;


public class ESQueryBuilder {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ESQueryBuilder.class);

	private static final String BOOST_OPERATOR = "^";
	private static final int CONTAINER_NAME_FLD_BOOST = 5;
	private static final int TAGS_FLD_BOOST = 5;
	
	private String searchText;
	private TemplateFacade template;
	private int pageSize = 10;
	private int pageNum = 0;
	
	private ESDataView view;
	
	private ESQueryBuilder(String searchText, TemplateFacade template) {
		this.searchText = searchText;
		this.template = template;
	}
	
	public static ESQueryBuilder builder(String searchText, TemplateFacade template) {
		ESQueryBuilder builder = new ESQueryBuilder(searchText, template);
		return builder;
	}
	
	public ESQueryBuilder withPagination(int pageSize, int pageNum) {
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		return this;
	}
	
	public ESQueryBuilder withViewScope(ESDataView view) {
		this.view = view;
		return this;
	}
	
	public SearchRequest build() {
		
		String indexName = getIndexName();
		Collection<String> types = getTypes();
		
		SearchRequest searchRequest = Requests.searchRequest(indexName)
					.searchType(SearchType.DFS_QUERY_THEN_FETCH)
					.types(types.toArray(new String[0]));
		
		Map<String, Integer> fieldBoostIndex = new HashMap<>();
		
		ContentTemplate contentTemplate = template.getTemplate();
		
		getFieldNames(fieldBoostIndex, contentTemplate.getDataDefinition().getContainer());
		
		MultiMatchQueryBuilder multiMatchQuery = QueryBuilders.multiMatchQuery(
				searchText, convertToMultiMatchBoostedFields(fieldBoostIndex));
		
		if (ESPropertiesUtil.getProperties().isFuzzyMatchOn() 
				&& this.searchText.length() >= ESPropertiesUtil.getProperties().getFuzzyMatchMinChars()) {
			multiMatchQuery.fuzziness(Fuzziness.AUTO);
		}
		
		QueryBuilder searchQuery = multiMatchQuery;
		if (view != null) {
			ESDataViewOperation viewOperation = new ESDataViewOperation(view);
			searchQuery = viewOperation.createViewScopedQuery(multiMatchQuery, types);
		}
		
		//MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("_all", searchText);
		SearchSourceBuilder searchSource = SearchSourceBuilder.searchSource().query(searchQuery);
		searchSource.size(pageSize);
		searchSource.from(pageNum * pageSize);
		
		searchRequest.source(searchSource);
		
		if (LOGGER.isDebugEnabled()) {
			
			LOGGER.debug("Search index: {}", indexName);
			LOGGER.debug("Search types: {}", types);
			LOGGER.debug("Search query: {}", searchSource);
		}
		
		return searchRequest;
	}
	
	private String[] convertToMultiMatchBoostedFields(Map<String, Integer> fieldBoostIndex) {
		
		String[] boostedFieldSearch = new String[fieldBoostIndex.size() + 2]; // +2 for container name and tags
		
		int indx = 0;
		for (Map.Entry<String, Integer> entry : fieldBoostIndex.entrySet()) {
			boostedFieldSearch[indx++] = entry.getKey() + BOOST_OPERATOR + entry.getValue();
		}
		
		// add field for search on container metadata
		boostedFieldSearch[indx++] = ContentDataConstants.CONTAINER_NAME_METADATA_FLD 
										+ BOOST_OPERATOR + CONTAINER_NAME_FLD_BOOST;
		
		// add field for tags search
		boostedFieldSearch[indx++] = ContentDataConstants.RECORD_TAGS_ATTR + "." + ContentDataConstants.TAG_NAME_ATTR
										+ BOOST_OPERATOR + TAGS_FLD_BOOST;

		
		return boostedFieldSearch;
	}
	
	private void getFieldNames(Map<String, Integer> fieldSearchBoost, List<ContainerType> containers) {
		
		for (ContainerType container : containers) {
			
			for (ContentItemType contentItem : container.getContentItem()) {
				
				if (contentItem.getType() == ContentItemClassType.DATE_TIME
						|| contentItem.getType() == ContentItemClassType.NUMERIC) {
					// do not search on Date and numeric fields
					continue;
				}
				
				Integer itemBoostValue = contentItem.getSearchBoost() == null ? 1 
											: contentItem.getSearchBoost().intValue();
				
				if (itemBoostValue != 0) { // ignore the fields which have 0 boost score
					
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
			}
		
			getFieldNames(fieldSearchBoost, container.getContainer());
		}
	}
	
	private String getESFieldName(ContentItemType contentItem, ContainerType container) {
		
		String fieldName = contentItem.getQualifiedId();
		
		ContainerType holdingContainer = container;
		if (!container.isReferenceable()) {
			holdingContainer = TemplateUtil.findReferenceableParentContainer(
					template.getTemplate().getDataDefinition(), container.getQualifiedId());
		}
		
		String containerQId = holdingContainer.getQualifiedId();

		// container name = c1.c2
		// field name = c1.c2.f1, hence remove the container prefix from field
		if (fieldName.startsWith(containerQId)) {
			fieldName = fieldName.substring(containerQId.length() + 1);
		}
		
		if (contentItem.getType() == ContentItemClassType.BOUNDED 
				|| contentItem.getType() == ContentItemClassType.CONTENT_STACK) {
			fieldName += ".label";
		} else if (contentItem.getType() == ContentItemClassType.DOC) {
			fieldName += ".name";
		}
		
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("ES Query field name: {}", fieldName);
		}
		
		return fieldName;
	}

	private String getIndexName() {
		return ElasticSearchUtil.getIndexName();
	}
	
	private Collection<String> getTypes() {
		
		Collection<String> allTypes = template.getAllCollectionNames();
		
		if (view != null) {
			allTypes = view.checkAndReturnVisibleTypes(allTypes);
		}
		
		return allTypes;
	}
	
}
