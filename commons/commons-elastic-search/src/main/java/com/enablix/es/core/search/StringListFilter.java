package com.enablix.es.core.search;

import java.util.List;

import com.enablix.commons.util.StringUtil;
import com.enablix.core.api.ConditionOperator;

public class StringListFilter extends SearchCondition<List<String>> {

	private static final ConditionOperator[] SUPPORTED_OP = {
		ConditionOperator.IN, ConditionOperator.NOT_IN};
	
	protected StringListFilter() { }
	
	public StringListFilter(String propName, List<String> propValue, ConditionOperator operator) {
		super(propName, propValue, operator == null ? ConditionOperator.IN : operator);
	}

	public StringListFilter(String propName, List<String> propValue, String operator) {
		super(propName, propValue, StringUtil.hasText(operator) ? operator : "IN");
	}

	@Override
	protected ConditionOperator[] supportedOperators() {
		return SUPPORTED_OP;
	}

}
