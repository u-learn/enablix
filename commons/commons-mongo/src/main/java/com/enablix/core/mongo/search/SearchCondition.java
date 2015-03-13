package com.enablix.core.mongo.search;


public abstract class SearchCondition<T> extends AbstractFilter {

	private String propertyName;
	
	private T propertyValue;
	
	private ConditionOperator operator;
	
	protected SearchCondition() { }
	
	public SearchCondition(String propName, T propValue, ConditionOperator operator) {
		this.propertyName = propName;
		this.propertyValue = propValue;
		setOperator(operator);
	}
	
	public SearchCondition(String propName, T propValue, String operator) {
		this(propName, propValue, ConditionOperator.parse(operator));
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public T getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(T propertyValue) {
		this.propertyValue = propertyValue;
	}

	public ConditionOperator getOperator() {
		return operator;
	}

	public void setOperator(ConditionOperator operator) {
		if (operator == null || !operator.in(supportedOperators())) {
			throw new IllegalArgumentException("invalid operator for " + this + " filter");
		} 
		this.operator = operator;
	}
	
	protected abstract ConditionOperator[] supportedOperators();
}
