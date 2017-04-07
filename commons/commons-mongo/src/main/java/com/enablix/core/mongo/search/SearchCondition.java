package com.enablix.core.mongo.search;

import java.util.Collection;

import org.springframework.data.mongodb.core.query.Criteria;


public abstract class SearchCondition<T> extends AbstractFilter {

	private String propertyName;
	
	private T propertyValue;
	
	private ConditionOperator operator;
	
	private Boolean existsCheck;
	
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
	
	public Boolean getExistsCheck() {
		return existsCheck;
	}

	public void setExistsCheck(Boolean existsCheck) {
		this.existsCheck = existsCheck;
	}

	@Override
	public Criteria toPredicate(Criteria root) {
		
		Criteria criteria = Criteria.where(getPropertyName());
		
		switch (getOperator()) {
		case EQ:
			criteria.is(getPropertyValue());
			break;

		case NOT_EQ:
			criteria.ne(getPropertyValue());
			break;
		
		case GT:
			criteria.gt(getPropertyValue());
			break;	
		
		case GTE:
			criteria.gte(getPropertyValue());
			break;	
			
		case LT:
			criteria.lt(getPropertyValue());
			break;	
			
		case LTE:
			criteria.lte(getPropertyValue());
			break;	
		
		case IN:
			criteria.in((Collection<?>) getPropertyValue());
			break;	
			
		case NOT_IN:
			criteria.nin((Collection<?>) getPropertyValue());
			break;
			
		case EXISTS:
			criteria = new Criteria().orOperator(Criteria.where(propertyName).exists((Boolean) getPropertyValue()), criteria);
			
		default:
			criteria.is(getPropertyValue());
			break;
		}
		
		if (existsCheck != null) {
			criteria = new Criteria().orOperator(Criteria.where(propertyName).exists(existsCheck), criteria);
			//criteria.orOperator(Criteria.where(propertyName).exists(existsCheck));
		}
		
		return criteria;
	}
	
	protected abstract ConditionOperator[] supportedOperators();
}
