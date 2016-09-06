package com.enablix.core.mongo.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class SearchFilterTest {

	public static void main(String[] args) {
		StringFilter f1 = new StringFilter("product.name", "AML", ConditionOperator.EQ);
		DateFilter f2 = new DateFilter("product.date", new Date(), ConditionOperator.LT);
		
		Collection<String> pnames = new ArrayList<>();
		pnames.add("AML");
		pnames.add("KYC");
		
		CollectionFilter<String> f3 = new CollectionFilter<>("product.name", pnames, ConditionOperator.IN);
		SearchCriteria c = new SearchCriteria(f3);
		c.and(f2).or(f1);
		
		System.out.println(Query.query(c.toPredicate(new Criteria())));
	}
	
}
