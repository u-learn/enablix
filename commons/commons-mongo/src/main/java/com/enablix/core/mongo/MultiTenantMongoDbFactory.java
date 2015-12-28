package com.enablix.core.mongo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.enablix.commons.util.tenant.TenantUtil;
import com.mongodb.DB;
import com.mongodb.Mongo;

public class MultiTenantMongoDbFactory extends SimpleMongoDbFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(MultiTenantMongoDbFactory.class);
	
	public MultiTenantMongoDbFactory(Mongo mongo, String databaseName) {
		super(mongo, databaseName);
	}
	
	@Override 
	public DB getDb(String dbName) {
		return super.getDb(getTenantAwareDbName(dbName));
	}
	
	private String getTenantAwareDbName(String dbName) {
		String tenantId = TenantUtil.getTenantId();
		LOGGER.trace("Tenant id : {}", tenantId);
		return tenantId == null ? dbName : tenantId + "_" + dbName;
	}

}
