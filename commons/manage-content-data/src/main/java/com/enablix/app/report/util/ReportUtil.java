package com.enablix.app.report.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.enablix.core.mongo.dao.GenericDao;
import com.enablix.core.mongo.search.BoolFilter;
import com.enablix.core.mongo.search.ConditionOperator;

@Component
public class ReportUtil {
	
	@Autowired
	private GenericDao genericDao ;
	
	public void updateLastestFlagOfOldRecords(Class<?> className) {
		
		BoolFilter latestTrue = new BoolFilter("latest", Boolean.TRUE, ConditionOperator.EQ);
		Query query = new Query(latestTrue.toPredicate(new Criteria()));

		Update update = new Update();
		update.set("latest", Boolean.FALSE);

		genericDao.updateMulti(query, update, className);

	}
}
