package com.enablix.analytics.correlation.matcher.mongo;

import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemClassType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.commons.xsdtopojo.FilterType;
import com.enablix.services.util.template.TemplateWrapper;

public class ContentFilterAttributeIdResolver implements FilterAttributIdResolver {

	@Override
	public String resolveFilterAttributeId(FilterType filter, String targetItemQId, TemplateWrapper template) {
		
		ContainerType targetContainer = template.getContainerDefinition(targetItemQId);
		ContentItemType filterContentItem = null;
		
		String filterAttrId = filter.getAttributeId();
		
		for (ContentItemType contentItem : targetContainer.getContentItem()) {
			if (contentItem.getId().equals(filterAttrId)) {
				filterContentItem = contentItem;
				break;
			}
		}
		
		filterAttrId = createContentItemAttrId(filterContentItem, filterAttrId);
		
		return filterAttrId;
	}
	
	protected String createContentItemAttrId(ContentItemType contentItem, String filterId) {
		
		if (contentItem != null && contentItem.getType() == ContentItemClassType.BOUNDED) {
			return filterId + "." + ContentDataConstants.ID_FLD_KEY;
		}
		
		return filterId;
	}

}
