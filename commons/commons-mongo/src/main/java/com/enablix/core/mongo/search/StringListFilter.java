package com.enablix.core.mongo.search;

import java.util.List;

import com.enablix.core.api.ConditionOperator;

public class StringListFilter extends SearchCondition<List<String>> {

	private static final ConditionOperator[] SUPPORTED_OP = {
		ConditionOperator.IN, ConditionOperator.NOT_IN};
	
	protected StringListFilter() { }
	
	public StringListFilter(String propName, List<String> propValue, ConditionOperator operator) {
		super(propName, propValue, operator);
	}

	public StringListFilter(String propName, List<String> propValue, String operator) {
		super(propName, propValue, operator);
	}

	@Override
	protected ConditionOperator[] supportedOperators() {
		return SUPPORTED_OP;
	}

}
