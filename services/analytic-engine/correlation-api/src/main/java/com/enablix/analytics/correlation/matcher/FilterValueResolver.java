package com.enablix.analytics.correlation.matcher;

import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.FilterType;

public interface FilterValueResolver {

	Object resolve(FilterType filter, MatchInputRecord matchInput, TemplateFacade template);
	
	boolean canHandle(FilterType filter);
	
}
