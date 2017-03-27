package com.enablix.analytics.correlation.matcher.mongo;

import com.enablix.core.commons.xsdtopojo.ContentItemType;

public class UserFilterAttributeIdResolver extends ContentFilterAttributeIdResolver {

	protected String createContentItemAttrId(ContentItemType contentItem, String filterId) {
		
		filterId = super.createContentItemAttrId(contentItem, filterId);
		
		if (contentItem != null) {
			filterId = "businessProfile.attributes." + filterId;
		}
		
		return filterId;
	}
	
}
