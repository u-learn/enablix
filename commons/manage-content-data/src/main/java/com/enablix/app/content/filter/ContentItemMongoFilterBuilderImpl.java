package com.enablix.app.content.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.core.api.ConditionOperator;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.mongo.search.SearchFilter;
import com.enablix.core.mongo.search.StringFilter;
import com.enablix.core.mongo.search.StringListFilter;

@Component
public class ContentItemMongoFilterBuilderImpl implements ContentItemMongoFilterBuilder {

	@Autowired
	private ContentFilterIdResolver filterIdResolver;
	
	@Override
	public SearchFilter createFilter(ContentItemType contentItem, Object value, TemplateFacade template) {

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
		
		String filterAttrId = filterIdResolver.resolveFilterAttributeId(contentItem, template);
		
		SearchFilter searchFilter = null;
		
		if (listValue != null && !listValue.isEmpty()) {
			
			searchFilter = new StringListFilter(filterAttrId, listValue, ConditionOperator.IN);
			
		} else {
			
			if (value == null) {
				value = "~~!@#4567dsfdf"; // random value so that it does not match
			}
			
			searchFilter = new StringFilter(filterAttrId, String.valueOf(value), ConditionOperator.EQ);
		}
		
		return searchFilter;
	}

}
