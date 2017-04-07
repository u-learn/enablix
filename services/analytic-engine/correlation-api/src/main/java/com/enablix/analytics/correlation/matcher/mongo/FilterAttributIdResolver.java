package com.enablix.analytics.correlation.matcher.mongo;

import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.FilterType;

public interface FilterAttributIdResolver {

	String resolveFilterAttributeId(FilterType filter, String targetItemQId, TemplateFacade template);
	
}
