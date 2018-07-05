package com.enablix.es.core.search;

import com.enablix.core.api.ConditionOperator;

public class NumericFilter extends SearchCondition<Number> {

	private static final ConditionOperator[] SUPPORTED_OP = {
		ConditionOperator.EQ, ConditionOperator.NOT_EQ,
		ConditionOperator.GT, ConditionOperator.GTE,
		ConditionOperator.LT, ConditionOperator.LTE};
	
	protected NumericFilter() { }
	
	public NumericFilter(String propName, Number propValue, ConditionOperator operator) {
		super(propName, propValue, operator);
	}
	
	public NumericFilter(String propName, Number propValue, String operator) {
		super(propName, propValue, operator);
	}

	@Override
	protected ConditionOperator[] supportedOperators() {
		return SUPPORTED_OP;
	}

}
