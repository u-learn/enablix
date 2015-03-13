package com.enablix.core.mongo.search;

import org.springframework.data.mongodb.core.query.Criteria;

public class NumericFilter extends SearchCondition<Number> {

	protected NumericFilter() { }
	
	public NumericFilter(String propName, Number propValue, ConditionOperator operator) {
		super(propName, propValue, operator);
	}
	
	public NumericFilter(String propName, Number propValue, String operator) {
		super(propName, propValue, operator);
	}

	@Override
	public Criteria toPredicate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ConditionOperator[] supportedOperators() {
		// TODO Auto-generated method stub
		return null;
	}

}
