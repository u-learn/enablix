package com.enablix.analytics.correlation.matcher.mongo;

import com.enablix.core.commons.xsdtopojo.FilterType;
import com.enablix.services.util.template.TemplateWrapper;

public interface FilterAttributIdResolver {

	String resolveFilterAttributeId(FilterType filter, String targetItemQId, TemplateWrapper template);
	
}
