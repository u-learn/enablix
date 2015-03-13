package com.enablix.core.mongo.search;

import java.util.Date;

import org.springframework.data.mongodb.core.query.Criteria;

public class DateFilter extends SearchCondition<Date> {

	protected DateFilter() { }
	
	public DateFilter(String propName, Date propValue, ConditionOperator operator) {
		super(propName, propValue, operator);
	}
	
	public DateFilter(String propName, Date propValue, String operator) {
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
