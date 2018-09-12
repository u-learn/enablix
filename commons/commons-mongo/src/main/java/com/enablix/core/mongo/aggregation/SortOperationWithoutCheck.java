package com.enablix.core.mongo.aggregation;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class SortOperationWithoutCheck implements AggregationOperation {

	private Sort sort;
	
	public SortOperationWithoutCheck(Sort sort) {
		super();
		this.sort = sort;
	}

	@Override
	public DBObject toDBObject(AggregationOperationContext context) {
		BasicDBObject object = new BasicDBObject();

		for (Order order : sort) {
			object.put(order.getProperty(), order.isAscending() ? 1 : -1);
		}

		return new BasicDBObject("$sort", object);
	}

}
