package com.enablix.analytics.correlation.matcher;

import com.enablix.core.commons.xsdtopojo.FilterType;
import com.enablix.services.util.template.TemplateWrapper;

public interface FilterValueResolver {

	Object resolve(FilterType filter, MatchInputRecord matchInput, TemplateWrapper template);
	
	boolean canHandle(FilterType filter);
	
}
