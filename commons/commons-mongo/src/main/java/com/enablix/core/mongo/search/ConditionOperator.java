package com.enablix.core.mongo.search;

import java.util.Collection;

import com.enablix.commons.util.collection.CollectionUtil;

public enum ConditionOperator {

	EQ, NOT_EQ, LT, GT, LTE, GTE, IN, NOT_IN;
	
	public static ConditionOperator parse(String operator) {
		return ConditionOperator.valueOf(operator);
	}
	
	public boolean in(ConditionOperator... operators) {
		return in(CollectionUtil.asList(operators));
	}
	
	public boolean in(Collection<ConditionOperator> operators) {
		if (CollectionUtil.isNotEmpty(operators)) {
			return operators.contains(this);
		}
		return false;
	}
	
}
