package com.enablix.core.mongo;

import java.util.regex.Pattern;

import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.enablix.core.mongo.dao.GenericDao;
import com.enablix.core.mongo.search.BoolFilter;
import com.enablix.core.mongo.search.ConditionOperator;
import com.enablix.core.mongo.view.MongoDataView;

public class MongoUtil {

	public static boolean isMultiTenantMongoDbFactory(MongoDbFactory mongoDbFactory) {
		return mongoDbFactory instanceof MultiTenantMongoDbFactory;
	}
	
	public static <T> T executeWithDataViewScope(
			MongoDataView dataView, DataViewOperation<T> operation) {
		
		T result = null;
		
		try {
			MongoDataViewContext.initialize(dataView);
			result = operation.execute();
		} finally {
			MongoDataViewContext.clear();
		}
		
		return result;
	}
	
	public static interface DataViewOperation<T> {
		T execute();
	}
	
	public static Pattern caseInsensitiveMatchRegexPattern(String text) {
		return Pattern.compile("^" + text + "$", Pattern.CASE_INSENSITIVE);
	}
	
	public static void updateLastestFlagOfOldRecords(GenericDao dao, Class<?> className) {
		
		BoolFilter latestTrue = new BoolFilter("latest", Boolean.TRUE, ConditionOperator.EQ);
		Query query = new Query(latestTrue.toPredicate(new Criteria()));

		Update update = new Update();
		update.set("latest", Boolean.FALSE);

		dao.updateMulti(query, update, className);

	}
	
	public static void updateLastestFlagOfOldRecords(GenericDao dao, Class<?> className, Criteria filter) {
		
		BoolFilter latestTrue = new BoolFilter("latest", Boolean.TRUE, ConditionOperator.EQ);
		
		Criteria queryCriteria = latestTrue.toPredicate(new Criteria());
		if (filter != null) {
			queryCriteria = queryCriteria.andOperator(filter);
		}
		
		Query query = new Query(queryCriteria);

		Update update = new Update();
		update.set("latest", Boolean.FALSE);

		dao.updateMulti(query, update, className);

	}
	
}
