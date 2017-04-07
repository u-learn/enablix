package com.enablix.app.content.filter;

import org.springframework.stereotype.Component;

import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContentItemClassType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;

@Component
public class ContentFilterIdResolverImpl implements ContentFilterIdResolver {

	@Override
	public String resolveFilterAttributeId(ContentItemType contentItem, TemplateFacade template) {
		
		String filterId = contentItem.getId();
		
		if (contentItem.getType() == ContentItemClassType.BOUNDED) {
			return filterId + "." + ContentDataConstants.ID_FLD_KEY;
		}
		
		return filterId;
	}

}
