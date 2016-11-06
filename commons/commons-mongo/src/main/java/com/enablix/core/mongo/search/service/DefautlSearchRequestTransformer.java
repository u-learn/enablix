package com.enablix.core.mongo.search.service;

import java.util.Map;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.enablix.core.mongo.search.SearchFilter;
import com.enablix.core.mongo.search.service.SearchRequest.FilterMetadata;

@Component
public class DefautlSearchRequestTransformer implements SearchRequestTransformer {

	public Criteria buildQueryCriteria(SearchRequest request) {
		
		SearchFilter queryFilter = null;
		
		Map<String, FilterMetadata> filterMetadata = request.getFilterMetadata();
		
		for (Map.Entry<String, Object> entry : request.getFilters().entrySet()) {
			
			FilterMetadata metadata = filterMetadata.get(entry.getKey());
			
			if (metadata == null) {
				metadata = new FilterMetadata();
				metadata.setField(entry.getKey());
			}
			
			SearchFilter searchFilter = metadata.buildSearchFilter(entry.getValue());
			
			if (queryFilter == null) {
				queryFilter = searchFilter;
			} else {
				queryFilter = queryFilter.and(searchFilter);
			}
		}
		
		Criteria criteria = new Criteria();
		if (queryFilter != null) {
			criteria = queryFilter.toPredicate(criteria);
		}
		
		return criteria;
		
	}
	
}
