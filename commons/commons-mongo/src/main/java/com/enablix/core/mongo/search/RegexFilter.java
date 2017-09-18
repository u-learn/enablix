package com.enablix.core.mongo.search;

import java.util.regex.Pattern;

public class RegexFilter extends SearchCondition<Pattern> {

	private static final ConditionOperator[] SUPPORTED_OP = {
		ConditionOperator.REGEX
	};
	
	protected RegexFilter() { }
	
	public RegexFilter(String propName, Pattern propValue, ConditionOperator operator) {
		super(propName, propValue, operator);
	}

	public RegexFilter(String propName, Pattern propValue, String operator) {
		super(propName, propValue, operator);
	}

	@Override
	protected ConditionOperator[] supportedOperators() {
		return SUPPORTED_OP;
	}

}
