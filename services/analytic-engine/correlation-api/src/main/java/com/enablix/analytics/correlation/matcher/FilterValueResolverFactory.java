package com.enablix.analytics.correlation.matcher;

import com.enablix.core.commons.xsdtopojo.FilterType;

public interface FilterValueResolverFactory {

	public FilterValueResolver getResolver(FilterType filter);
	
}
