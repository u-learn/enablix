package com.enablix.analytics.search.es;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import com.enablix.analytics.search.SearchInput;
import com.enablix.analytics.search.SearchInput.ParentFilter;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.api.ConditionOperator;
import com.enablix.core.api.SearchRequest;
import com.enablix.core.api.SearchRequest.FilterMetadata;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.es.core.search.BoolFilter;
import com.enablix.es.core.search.NumericFilter;
import com.enablix.es.core.search.SearchQueryFilter;
import com.enablix.es.core.search.StringFilter;
import com.enablix.es.core.search.StringListFilter;
import com.enablix.services.util.TemplateUtil;

public class SearchRequestQueryBuilder implements MatchQueryBuilder {

	private SearchInput request;
	private MatchQueryBuilder stringMatchBuilder;
	private TemplateFacade template;
	
	public SearchRequestQueryBuilder(SearchInput searchRequest, TemplateFacade template) {
		this(searchRequest, template, null);
	}
	
	public SearchRequestQueryBuilder(SearchInput request, TemplateFacade template, MatchQueryBuilder stringMatchBuilder) {
		
		super();
		
		this.request = request;
		this.template = template;
		this.stringMatchBuilder = stringMatchBuilder;
		
		if (stringMatchBuilder == null) {
			this.stringMatchBuilder = new StringMultiMatchQueryBuilder(request.getRequest().getTextQuery());
		}
	}


	@Override
	public QueryBuilder buildQuery(String[] searchFields, MultiMatchQueryOptimizer queryOptimizer,
			FuzzyMatchOption fuzzyMatchOption) {
		
		SearchRequest searchRequest = request.getRequest();
		QueryBuilder textQuery = stringMatchBuilder.buildQuery(searchFields, queryOptimizer, fuzzyMatchOption);
		
		BoolQueryBuilder filterQuery = null;
		
		BoolQueryBuilder parentQuery = buildParentFilters();
		if (parentQuery != null) {
			filterQuery = QueryBuilders.boolQuery();
			filterQuery.filter(parentQuery);
		}
		
		if (CollectionUtil.isNotEmpty(searchRequest.getFilters())) {
			
			filterQuery = filterQuery == null ? QueryBuilders.boolQuery() : filterQuery;
		
			Map<String, FilterMetadata> filterMetadata = searchRequest.getFilterMetadata();
			
			for (Map.Entry<String, Object> entry : searchRequest.getFilters().entrySet()) {
				
				FilterMetadata metadata = filterMetadata.get(entry.getKey());
				
				if (metadata == null) {
					metadata = new FilterMetadata();
					metadata.setField(entry.getKey());
				}
				
				QueryBuilder queryBuilder = buildSearchFilter(metadata, entry.getValue());
				
				if (queryBuilder != null) {
					filterQuery.filter(queryBuilder);
				}
			}
			
		}
		
		if (filterQuery != null) {
			filterQuery.must(textQuery);
		}
		
		return filterQuery != null ? filterQuery : textQuery;
		
	}
	
	private BoolQueryBuilder buildParentFilters() {
		
		BoolQueryBuilder boolQuery = null;
		
		ParentFilter parent = this.request.getParent();
		
		if (parent != null) {
			
			ContainerType container = template.getContainerDefinition(parent.getQualifiedId());
			
			if (container != null) {
				
				Set<String> childItemIds = new HashSet<>();
				List<String> contentQIds = request.getContentQIds();
				
				container.getContainer().forEach((cc) -> {
					
					if (TemplateUtil.isLinkedContainer(cc) && 
							(CollectionUtil.isEmpty(contentQIds) || 
								contentQIds.contains(cc.getLinkContainerQId()))) {
						
						childItemIds.add(cc.getLinkContentItemId());
					}
				});
				
				if (CollectionUtil.isNotEmpty(childItemIds)) {
					boolQuery = QueryBuilders.boolQuery();
					for (String childId : childItemIds) {
						String filterId = childId + "." + ContentDataConstants.ID_FLD_KEY;
						boolQuery.should(QueryBuilders.matchQuery(filterId, parent.getIdentity()));
					}
					boolQuery.minimumNumberShouldMatch(1);
				}
			}
		}
		
		return boolQuery;
	}
	
	@SuppressWarnings("unchecked")
	private QueryBuilder buildSearchFilter(FilterMetadata metadata, Object filterValue) {
		
		SearchQueryFilter filter = null;
		
		switch (metadata.getDataType()) {
			
			case DATE:
				throw new UnsupportedOperationException("Date filter not supported for text query search");
				
			case BOOL:
				Boolean propValue = filterValue instanceof Boolean ? (Boolean) filterValue : 
										Boolean.valueOf(String.valueOf(filterValue));
				filter = new BoolFilter(metadata.getField(), propValue, metadata.getOperator());
				break;
				
			case NUMBER:
				
				Number numValue = null;
				
				if (filterValue instanceof Number) {
					numValue = (Number) filterValue;
				} else {
					numValue = Double.parseDouble(String.valueOf(filterValue));
				}
				
				filter = new NumericFilter(metadata.getField(), numValue, metadata.getOperator());
				break;
				
			case STRING:
				
				if (filterValue instanceof List) {
					
					if (metadata.getOperator() == ConditionOperator.EQ) {
						metadata.setOperator(ConditionOperator.IN);
					}
					
					filter = new StringListFilter(metadata.getField(), (List<String>) filterValue, metadata.getOperator());
					break;
				}
				
			default:
				filter = new StringFilter(metadata.getField(), String.valueOf(filterValue), metadata.getOperator());
				break;
		}
		
		return filter != null ? filter.toQueryBuilder() : null;
	}

}
