package com.enablix.app.report.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.core.mongo.MongoUtil;
import com.enablix.core.mongo.dao.GenericDao;

@Component
public class ReportUtil {
	
	@Autowired
	private GenericDao genericDao ;
	
	public void updateLastestFlagOfOldRecords(Class<?> className) {
		MongoUtil.updateLastestFlagOfOldRecords(genericDao, className);
	}
}
