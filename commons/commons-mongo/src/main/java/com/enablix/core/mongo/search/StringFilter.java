package com.enablix.core.mongo.search;

import org.springframework.data.mongodb.core.query.Criteria;

public class StringFilter extends SearchCondition<String> {

	protected StringFilter() { }
	
	public StringFilter(String propName, String propValue, ConditionOperator operator) {
		super(propName, propValue, operator);
	}

	public StringFilter(String propName, String propValue, String operator) {
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
