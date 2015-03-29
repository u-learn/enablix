package com.enablix.core.mongo.search;


public class StringFilter extends SearchCondition<String> {

	private static final ConditionOperator[] SUPPORTED_OP = {
		ConditionOperator.EQ, ConditionOperator.NOT_EQ,
		ConditionOperator.GT, ConditionOperator.GTE,
		ConditionOperator.LT, ConditionOperator.LTE};
	
	protected StringFilter() { }
	
	public StringFilter(String propName, String propValue, ConditionOperator operator) {
		super(propName, propValue, operator);
	}

	public StringFilter(String propName, String propValue, String operator) {
		super(propName, propValue, operator);
	}

	@Override
	protected ConditionOperator[] supportedOperators() {
		return SUPPORTED_OP;
	}

}
