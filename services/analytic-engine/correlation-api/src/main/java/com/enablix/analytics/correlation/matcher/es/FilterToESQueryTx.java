package com.enablix.analytics.correlation.matcher.es;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.analytics.correlation.matcher.FilterValueResolver;
import com.enablix.analytics.correlation.matcher.FilterValueResolverFactory;
import com.enablix.analytics.correlation.matcher.MatchInputRecord;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemClassType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.commons.xsdtopojo.FilterType;

@Component
public class FilterToESQueryTx {

	@Autowired
	private FilterValueResolverFactory resolverFactory;
	
	public List<QueryBuilder> createESQuery(FilterType filter, String targetItemQId, 
			MatchInputRecord matchInput, TemplateFacade template) {
		
		FilterValueResolver resolver = resolverFactory.getResolver(filter);
		Object value = resolver.resolve(filter, matchInput, template);
		List<String> listValue = null;
		
		if (value instanceof Collection) {
			
			Collection<?> collValue = (Collection<?>) value;
			
			listValue = new ArrayList<>();
			
			for (Object collItem : collValue) {
			
				if (collItem instanceof String) {
					
					listValue.add((String) collItem);
					
				} else if (collItem instanceof Map<?, ?>) {
					
					Map<?, ?> mapCollItem = (Map<?, ?>) collItem;
					Object itemId = mapCollItem.get(ContentDataConstants.ID_FLD_KEY);
					
					if (itemId != null) {
					
						listValue.add(String.valueOf(itemId));
						
					} else {
						
						Object itemIdentity = mapCollItem.get(ContentDataConstants.IDENTITY_KEY);
						if (itemIdentity != null) {
							listValue.add(String.valueOf(itemIdentity));
						}
					}
				}
			}
		}
		
		String filterAttrId = extractFilterAttributeId(filter, targetItemQId, template);
		
		List<QueryBuilder> qbs = new ArrayList<>();
		
		if (listValue != null && !listValue.isEmpty()) {
			
			for (String valueItem : listValue) {
				qbs.add(QueryBuilders.matchQuery(filterAttrId, valueItem));
			}
			
		} else {
			
			if (value == null || CollectionUtil.isCollectionAndEmpty(value)) {
				value = "~~!@#4567dsfdf"; // random value so that it does not match
			}
			qbs.add(QueryBuilders.matchQuery(filterAttrId, value));
		}
		
		return qbs;
	}

	private String extractFilterAttributeId(FilterType filter, String targetItemQId, TemplateFacade template) {
		
		ContainerType targetContainer = template.getContainerDefinition(targetItemQId);
		ContentItemType filterContentItem = null;
		
		String filterAttrId = filter.getAttributeId();
		
		for (ContentItemType contentItem : targetContainer.getContentItem()) {
			if (contentItem.getId().equals(filterAttrId)) {
				filterContentItem = contentItem;
				break;
			}
		}
		
		if (filterContentItem != null) {
			if (filterContentItem.getType() == ContentItemClassType.BOUNDED) {
				filterAttrId += "." + ContentDataConstants.ID_FLD_KEY;
			}
		}
		
		return filterAttrId;
	}
	
}
