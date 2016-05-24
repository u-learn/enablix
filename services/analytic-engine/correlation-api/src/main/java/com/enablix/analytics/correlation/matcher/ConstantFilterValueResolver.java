package com.enablix.analytics.correlation.matcher;

import org.springframework.stereotype.Component;

import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.commons.xsdtopojo.FilterType;

@Component
public class ConstantFilterValueResolver implements FilterValueResolver {

	@Override
	public Object resolve(FilterType filter, MatchInputRecord matchInput, ContentTemplate template) {
		return filter.getConstant().getValue();
	}

	@Override
	public boolean canHandle(FilterType filter) {
		return filter.getConstant() != null;
	}


}
