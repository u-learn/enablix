package com.enablix.analytics.correlation.matcher;

import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.commons.xsdtopojo.FilterType;

public interface FilterValueResolver {

	Object resolve(FilterType filter, MatchInputRecord matchInput, ContentTemplate template);
	
	boolean canHandle(FilterType filter);
	
}
