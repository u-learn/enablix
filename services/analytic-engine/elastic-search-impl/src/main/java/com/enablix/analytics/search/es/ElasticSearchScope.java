package com.enablix.analytics.search.es;

import java.util.ArrayList;
import java.util.List;

import com.enablix.analytics.search.SearchInput;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.api.TemplateFacade;

public class ElasticSearchScope {

	private SearchInput scope;
	
	private TemplateFacade template;

	public ElasticSearchScope(SearchInput scope, TemplateFacade template) {
		super();
		this.scope = scope;
		this.template = template;
	}
	
	public TypeFilter getTypeFilter() {
		
		if (scope != null && CollectionUtil.isNotEmpty(scope.getContentQIds())) {
			
			List<String> typeCollections = new ArrayList<>(); 
			
			scope.getContentQIds().forEach((cqId) -> {
				String collectionName = template.getCollectionName(cqId);
				if (StringUtil.hasText(collectionName)) {
					typeCollections.add(collectionName);
				}
			});
			
			return (searchType) -> typeCollections.contains(searchType);
		}
		
		return null;
	}
	
}
