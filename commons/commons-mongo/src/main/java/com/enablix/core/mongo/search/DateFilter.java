package com.enablix.core.mongo.search;

import java.util.Date;

public class DateFilter extends SearchCondition<Date> {

	private static final ConditionOperator[] SUPPORTED_OP = {
		ConditionOperator.EQ, ConditionOperator.NOT_EQ,
		ConditionOperator.GT, ConditionOperator.GTE,
		ConditionOperator.LT, ConditionOperator.LTE};
	
	protected DateFilter() { }
	
	public DateFilter(String propName, Date propValue, ConditionOperator operator) {
		super(propName, propValue, operator);
	}
	
	public DateFilter(String propName, Date propValue, String operator) {
		super(propName, propValue, operator);
	}

	@Override
	protected ConditionOperator[] supportedOperators() {
		return SUPPORTED_OP;
	}

}
