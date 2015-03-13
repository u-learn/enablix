package com.enablix.core.mongo.search;

import java.util.Collection;

import org.springframework.data.mongodb.core.query.Criteria;

public class CollectionFilter<T> extends SearchCondition<Collection<T>> {

	private static final ConditionOperator[] SUPPORTED_OP = {ConditionOperator.IN, ConditionOperator.NOT_IN};
	
	protected CollectionFilter() { }
	
	public CollectionFilter(String propName, Collection<T> propValue, ConditionOperator operator) {
		super(propName, propValue, operator);
	}

	public CollectionFilter(String propName, Collection<T> propValue, String operator) {
		super(propName, propValue, operator);
	}
	
	@Override
	public Criteria toPredicate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ConditionOperator[] supportedOperators() {
		return SUPPORTED_OP;
	}

}
