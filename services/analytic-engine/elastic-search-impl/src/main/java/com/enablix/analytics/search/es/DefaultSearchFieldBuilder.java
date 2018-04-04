package com.enablix.analytics.search.es;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemClassType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.services.util.TemplateUtil;

public class DefaultSearchFieldBuilder implements SearchFieldBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSearchFieldBuilder.class);
	
	private static final String BOOST_OPERATOR = "^";
	private static final int CONTAINER_NAME_FLD_BOOST = 5;
	private static final int TAGS_FLD_BOOST = 5;
	
	@Override
	public String[] getContentSearchFields(SearchFieldFilter filter, TemplateFacade template) {
		
		Map<String, Integer> fieldBoostIndex = new HashMap<>();
		ContentTemplate contentTemplate = template.getTemplate();
		
		addContainerFields(fieldBoostIndex, contentTemplate.getDataDefinition().getContainer(), filter, template);
		addSystemIntroducedFields(filter, fieldBoostIndex);
		
		return convertToMultiMatchBoostedFields(fieldBoostIndex);
	}
	
	private void addSystemIntroducedFields(SearchFieldFilter filter, Map<String, Integer> fieldBoostIndex) {
		
		// add field for search on container metadata
		if (filter.searchIn(ContentDataConstants.CONTAINER_NAME_METADATA_FLD)) {
			fieldBoostIndex.put(ContentDataConstants.CONTAINER_NAME_METADATA_FLD, CONTAINER_NAME_FLD_BOOST);
		}
		
		// add field for tags search
		if (filter.searchIn(ContentDataConstants.RECORD_TAGS_ATTR)) {
			fieldBoostIndex.put(ContentDataConstants.RECORD_TAGS_ATTR, TAGS_FLD_BOOST);
		}
		
	}
	
	private void addContainerFields(Map<String, Integer> fieldSearchBoost, 
			List<ContainerType> containers, SearchFieldFilter filter, TemplateFacade template) {
		
		for (ContainerType container : containers) {
			
			for (ContentItemType contentItem : container.getContentItem()) {
				
				if (contentItem.getType() == ContentItemClassType.DATE_TIME
						|| contentItem.getType() == ContentItemClassType.NUMERIC
						|| !filter.searchIn(container.getQualifiedId(), contentItem)) {
					// do not search on Date and numeric fields
					continue;
				}
				
				Integer itemBoostValue = contentItem.getSearchBoost() == null ? 1 
											: contentItem.getSearchBoost().intValue();
				
				if (itemBoostValue != 0) { // ignore the fields which have 0 boost score
					
					String esFieldName = getESFieldName(contentItem, container, template);
					
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
		
			addContainerFields(fieldSearchBoost, container.getContainer(), filter, template);
		}
	}
	
	private String getESFieldName(ContentItemType contentItem, ContainerType container, TemplateFacade template) {
		
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
	
	private String[] convertToMultiMatchBoostedFields(Map<String, Integer> fieldBoostIndex) {
		
		String[] boostedFieldSearch = new String[fieldBoostIndex.size()];
		
		int indx = 0;
		for (Map.Entry<String, Integer> entry : fieldBoostIndex.entrySet()) {
			boostedFieldSearch[indx++] = entry.getKey() + BOOST_OPERATOR + entry.getValue();
		}
		
		return boostedFieldSearch;
	}

}
