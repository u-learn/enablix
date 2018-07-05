package com.enablix.es.core.search;

import java.util.Collection;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import com.enablix.core.api.ConditionOperator;


public abstract class SearchCondition<T> extends AbstractQueryFilter {

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
	public QueryBuilder toQueryBuilder() {
		
		QueryBuilder qb = null;
		
		switch (getOperator()) {

		case NOT_EQ:
			qb = QueryBuilders.boolQuery().mustNot(QueryBuilders.matchQuery(getPropertyName(), getPropertyValue()));
			break;
		
		case EXISTS:
		case REGEX:
		case GT:
		case GTE:
		case LT:
		case LTE:
			throw new UnsupportedOperationException("Operator [" + getOperator() + "] not supported");	
		
		case IN:
			
			BoolQueryBuilder inQuery = QueryBuilders.boolQuery();
			
			((Collection<?>) getPropertyValue()).forEach(
					(val) -> inQuery.should(QueryBuilders.matchQuery(getPropertyName(), val)));
			
			inQuery.minimumNumberShouldMatch(1);
			qb = inQuery;
			
			break;	
			
		case NOT_IN:
			
			BoolQueryBuilder notInQuery = QueryBuilders.boolQuery();
			
			((Collection<?>) getPropertyValue()).forEach(
					(val) -> notInQuery.mustNot(QueryBuilders.matchQuery(getPropertyName(), val)));
			
			qb = notInQuery;
			break;
			
		case EQ:
		default:
			qb = QueryBuilders.matchQuery(getPropertyName(), getPropertyValue());
		
		}
			
		return qb;
	}
	
	protected abstract ConditionOperator[] supportedOperators();
}
