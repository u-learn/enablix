package com.enablix.es.core.search;

import com.enablix.core.api.ConditionOperator;

public class ObjectFilter extends SearchCondition<Boolean> {

	private static final ConditionOperator[] SUPPORTED_OP = { ConditionOperator.EXISTS };
	
	protected ObjectFilter() { }
	
	public ObjectFilter(String propName, boolean propValue, ConditionOperator operator) {
		super(propName, propValue, operator);
	}

	public ObjectFilter(String propName, boolean propValue, String operator) {
		super(propName, propValue, operator);
	}

	@Override
	protected ConditionOperator[] supportedOperators() {
		return SUPPORTED_OP;
	}

}
