package com.enablix.core.mongo.search;

import com.enablix.core.api.ConditionOperator;

public class BoolFilter extends SearchCondition<Boolean> {

	private static final ConditionOperator[] SUPPORTED_OP = {
		ConditionOperator.EQ, ConditionOperator.NOT_EQ};
	
	protected BoolFilter() { }
	
	public BoolFilter(String propName, Boolean propValue, ConditionOperator operator) {
		super(propName, propValue, operator);
	}

	public BoolFilter(String propName, Boolean propValue, String operator) {
		super(propName, propValue, operator);
	}

	@Override
	protected ConditionOperator[] supportedOperators() {
		return SUPPORTED_OP;
	}

}
